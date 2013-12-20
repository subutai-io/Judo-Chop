package org.safehaus.jchop;


import java.util.Set;

import org.reflections.Reflections;
import org.safehaus.perftest.CallStats;
import org.safehaus.perftest.IController;
import org.safehaus.perftest.api.CallStatsSnapshot;
import org.safehaus.perftest.api.RunInfo;
import org.safehaus.perftest.api.State;
import org.safehaus.perftest.api.TestInfo;
import org.safehaus.perftest.api.annotations.IterationChop;
import org.safehaus.perftest.api.annotations.TimeChop;
import org.safehaus.perftest.api.settings.ConfigKeys;
import org.safehaus.perftest.api.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;


/**
 * The ChopController controls the process of executing JChop tests.
 */
@Singleton
public class ChopController implements IController, Runnable {
    public static final long TIMEOUT = 1000L;

    private static final Logger LOG = LoggerFactory.getLogger( ChopController.class );
    private final Object lock = new Object();

    private Set<Class<?>> timeChopClasses;
    private Set<Class<?>> iterationChopClasses;
    private State state = State.READY;
    private long timeout = TIMEOUT; // @TODO should be configurable
    private ChopRunner<?> currentRunner;

    private StoreService service;
    private CallStats stats;
    private TestInfo testInfo;
    private RunInfo runInfo = new RunInfo( 0 );

    private long startTime;
    private long stopTime;


    @Inject
    private void setCallStats( CallStats stats ) {
        this.stats = stats;
    }

    @Inject
    private void setTestInfo( TestInfo testInfo ) {
        if ( testInfo == null ) {
            state = State.INACTIVE;
        }

        this.testInfo = testInfo;
    }


    @Inject
    private void setStoreService( StoreService service ) {
        this.service = service;
    }


    @Inject
    private void setBasePackage( @Named( ConfigKeys.TEST_PACKAGE_BASE ) String basePackage ) {
        Preconditions.checkNotNull( basePackage, "The basePackage cannot be null!" );
        Preconditions.checkArgument( basePackage.length() == 0,
                "The basePackage cannot be the empty string and must be a valid package." );

        Reflections reflections = new Reflections( basePackage );
        timeChopClasses = reflections.getTypesAnnotatedWith( TimeChop.class );
        iterationChopClasses = reflections.getTypesAnnotatedWith( IterationChop.class );

        if ( timeChopClasses.isEmpty() && iterationChopClasses.isEmpty() ) {
            state = State.INACTIVE;
        }
        else {
            state = State.READY;
        }
    }


    @Inject
    private void setTimeout( @Named( ConfigKeys.TEST_STOP_TIMEOUT ) long timeout ) {
        if ( timeout <= 0 ) {
            this.timeout = TIMEOUT;
        }
        else {
            this.timeout = timeout;
        }
    }


    @Override
    public void reset() {
        synchronized ( lock ) {
            if ( state == State.STOPPED ) {
                state = State.READY;
                runInfo = new RunInfo( runInfo.getRunNumber() + 1 );
                startTime = -1;
                stopTime = -1;
                currentRunner = null;
                stats.stop();
                stats.reset();
            }
        }
    }


    @Override
    public CallStatsSnapshot getCallStatsSnapshot() {
        return stats.getStatsSnapshot( isRunning(), getStartTime(), getStopTime() );
    }


    @Override
    // @TODO do not expose state to the outside (use isRunning() etc)
    public State getState() {
        return state;
    }


    @Override
    public RunInfo getRunInfo() {
        return runInfo;
    }


    @Override
    public boolean isRunning() {
        synchronized ( lock ) {
            return state == State.RUNNING;
        }
    }


    @Override
    public boolean needsReset() {
        synchronized ( lock ) {
            return state == State.STOPPED;
        }
    }


    @Override
    public long getStartTime() {
        return startTime;
    }


    @Override
    public long getStopTime() {
        return stopTime;
    }


    @Override
    public TestInfo getTestInfo() {
        return testInfo;
    }


    public void start() {
        synchronized ( lock ) {
            if ( state == State.READY ) {
                state = State.RUNNING;
                startTime = System.currentTimeMillis();
                new Thread( this ).start();
                lock.notifyAll();
            }
        }
    }


    @Override
    public void stop() {
        synchronized ( lock ) {
            if ( state == State.RUNNING ) {
                currentRunner.stop();
                if ( currentRunner.getState() != State.STOPPED ) {
                    LOG.warn( "Could not properly stop the current runner!" );
                }

                state = State.STOPPED;
                currentRunner = null;
                stopTime = System.currentTimeMillis();
                lock.notifyAll();
            }
        }
    }


    @Override
    public void run() {
        for ( Class<?> iterationTest : iterationChopClasses ) {
            synchronized ( lock ) {
                currentRunner = new IterationChopRunner( iterationTest );
                currentRunner.setTimeout( timeout );
                currentRunner.start();
                lock.notifyAll();
            }

            try {
                synchronized ( lock ) {
                    while ( currentRunner.blockTilDone( timeout ) ) {
                        if ( state == State.STOPPED ) {
                            currentRunner.stop();
                        }

                        lock.notifyAll();
                    }
                }
            }
            catch ( InterruptedException e ) {
                LOG.warn( "Awe snap! Someone woke me up early!" );
            }
        }

        for ( Class<?> timeTest : timeChopClasses ) {
            synchronized ( lock ) {
                currentRunner = new TimeChopRunner( timeTest );
                currentRunner.setTimeout( timeout );
                currentRunner.start();
                lock.notifyAll();
            }

            try {
                synchronized ( lock ) {
                    while ( currentRunner.blockTilDone( timeout ) ) {
                        if ( state == State.STOPPED ) {
                            currentRunner.stop();
                        }

                        lock.notifyAll();
                    }
                }
            }
            catch ( InterruptedException e ) {
                LOG.warn( "Awe snap! Someone woke me up early!" );
            }
        }

        LOG.info( "Test has stopped." );
        stopTime = System.currentTimeMillis();
        stats.stop();
        service.uploadResults( testInfo, runInfo, stats.getResultsFile() );
        reset();
    }
}
