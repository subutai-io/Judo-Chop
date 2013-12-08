package org.safehaus.perftest.client;


import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.safehaus.perftest.BaseResult;
import org.safehaus.perftest.Result;
import org.safehaus.perftest.RunInfo;
import org.safehaus.perftest.RunnerInfo;
import org.safehaus.perftest.State;
import org.safehaus.perftest.TestInfo;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.netflix.config.DynamicStringProperty;


/**
 * An implementation of the PerftestClient interface.
 */
@Singleton
public class PerftestClientImpl implements PerftestClient {
    // @Inject @Named( "formation" ) DynamicStringProperty formation;


    @Override
    public Set<RunnerInfo> getRunners( final String formation ) {
        return new HashSet<RunnerInfo>();
    }


    @Override
    public Set<TestInfo> getTests( final String formation ) {
        return new HashSet<TestInfo>();
    }


    @Override
    public Set<RunInfo> getRuns( final TestInfo test ) {
        return new HashSet<RunInfo>();
    }


    @Override
    public File getResults( final RunInfo run ) throws IOException {
        return File.createTempFile( "foo", "bar" );
    }


    @Override
    public void delete( final RunInfo run ) {
        throw new RuntimeException( "Not implemented yet" );
    }


    @Override
    public void delete( final TestInfo test ) {
        throw new RuntimeException( "Not implemented yet" );
    }


    @Override
    public Result load( final TestInfo test ) {
        return new BaseResult( "http://localhost:8080", true, "load success", State.READY );
    }


    @Override
    public Result stop( final RunnerInfo runner, final boolean propagate ) {
        return new BaseResult( "http://localhost:8080", true, "test stopped", State.STOPPED );
    }


    @Override
    public Result reset( final RunnerInfo runner, final boolean propagate ) {
        return new BaseResult( "http://localhost:8080", true, "test reset", State.READY );
    }


    @Override
    public Result scan( final RunnerInfo runner, final boolean propagate ) {
        return new BaseResult( "http://localhost:8080", true, "scan triggered", State.READY );
    }


    @Override
    public Result verify( final String formation ) {
        return new BaseResult( "http://localhost:8080", true, "verification triggered", State.READY );
    }
}