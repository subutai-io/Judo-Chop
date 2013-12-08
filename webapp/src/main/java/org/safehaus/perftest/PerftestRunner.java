package org.safehaus.perftest;


import org.safehaus.perftest.amazon.AmazonS3Service;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import org.safehaus.perftest.settings.PropSettings;
import org.safehaus.perftest.settings.TestInfoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * A Perftest runner.
 */
@Singleton
public class PerftestRunner implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger( PerftestRunner.class );


    private final AmazonS3Service service;
    private final Injector injector;
    private final Object lock = new Object();
    private long sleepToStop = PropSettings.getSleepToStop();
    private CallStats stats;
    private Perftest test;
    private ExecutorService executorService;
    private State state = State.READY;
    private long startTime;
    private long stopTime;

    private final TestInfo testInfo;
    private RunInfo runInfo;


    @Inject
    public PerftestRunner( Injector injector, TestModuleLoader loader, AmazonS3Service service )
    {
        this.injector = injector;
        this.service = service;
        test = loader.getChildInjector().getInstance( Perftest.class );
        testInfo = new TestInfoImpl( test, loader.getTestModule() );
        testInfo.setLoadTime( new Date().toString() );
        service.uploadTestInfo( testInfo );

        stats = injector.getInstance( CallStats.class );
        executorService = Executors.newFixedThreadPool( test.getThreadCount() );
        runInfo = new RunInfo( 0 );
    }


    public void reset() {
        synchronized ( lock ) {
            state = State.READY;
            startTime = 0;
            stopTime = 0;

            if ( stats != null )
            {
                stats.reset();
            }

            stats = injector.getInstance( CallStats.class );

            if ( runInfo == null ) {
                runInfo = new RunInfo( 0 );
            }
            else {
                runInfo = new RunInfo( runInfo.getRunNumber() );
            }
        }
    }


    public CallStatsSnapshot getCallStatsSnapshot() {
        return stats.getStatsSnapshot( isRunning(), getStartTime(), getStopTime() );
    }


    public State getState() {
        return state;
    }

    public RunInfo getRunInfo() {
        return runInfo;
    }


    public boolean isRunning() {
        synchronized ( lock ) {
            return state == State.RUNNING;
        }
    }


    public boolean needsReset() {
        synchronized ( lock )
        {
            return state == State.STOPPED;
        }
    }


    public long getStartTime() {
        return startTime;
    }


    public long getStopTime() {
        return stopTime;
    }


    public void start() {
        synchronized ( lock ) {
            state = State.RUNNING;
            startTime = System.nanoTime();

            for ( int ii = 0; ii < test.getThreadCount(); ii++) {
                executorService.execute( this );
            }
        }

        // launch a coordinator thread to detect when all others are done
        new Thread( new Runnable() {
            @Override
            public void run() {
                try {
                    while ( executorService.awaitTermination( sleepToStop, TimeUnit.MILLISECONDS ) ) {
                        LOG.info( "woke up state = {}", state );
                    }
                }
                catch ( InterruptedException e ) {
                    LOG.error( "Got interrupted while monitoring executor service.", e );
                }

                stats.stop();

                LOG.info( "COORDINATOR THREAD: all threads have died." );
                PerftestRunner.this.state = State.READY;
                stopTime = System.nanoTime();

                service.uploadResults( testInfo, runInfo, stats.getResultsFile() );
                testInfo.addRunInfo( runInfo );
            }
        } ).start();
    }


    public void stop() {
        synchronized ( lock ) {
            state = State.STOPPED;

            try {
                while ( executorService.awaitTermination( sleepToStop, TimeUnit.MILLISECONDS ) ) {
                    LOG.info( "woke up: state = {}", state );
                }
            }
            catch ( InterruptedException e ) {
                LOG.error( "Got interrupted while monitoring executor service.", e );
            }

            stats.stop();
            stopTime = System.nanoTime();
            state = State.STOPPED;
        }
    }

    @Override
    public void run() {
        long delay = test.getDelayBetweenCalls();

        while( state == State.RUNNING && ( stats.getCallCount() < test.getCallCount() ) ) {
            long startTime = System.nanoTime();
            test.call();
            long endTime = System.nanoTime();
            stats.callOccurred( test, startTime, endTime, TimeUnit.NANOSECONDS );

            if ( delay > 0 )
            {
                try {
                    Thread.sleep( delay );
                } catch ( InterruptedException e ) {
                    LOG.error( "Thread was interrupted.", e );
                }
            }

            synchronized ( lock ) {
                lock.notifyAll();
            }
        }
    }
}