package org.safehaus.chop.client.rest;


import java.util.Map;

import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.safehaus.chop.api.Result;
import org.safehaus.chop.api.Runner;
import org.safehaus.chop.api.store.StoreService;
import org.safehaus.chop.client.PerftestClientModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import static org.safehaus.chop.client.rest.RestRequests.status;


/**
 *
 */
@RunWith(JukitoRunner.class)
@UseModules(PerftestClientModule.class)
public class RestRequestsTest {
    private static final Logger LOG = LoggerFactory.getLogger( RestRequestsTest.class );
    @Inject
    StoreService service;


    @Before
    public void setup() {
        service.start();
    }


    @After
    public void tearDown() {
        service.stop();
    }


    @Test @Ignore
    public void testStart() {
        Map<String, Runner> runners = service.getRunners();

        if ( runners.size() == 0 ) {
            LOG.debug( "No drivers found, cannot start test" );
            return;
        }

        Runner firstRunner = runners.values().iterator().next();
        Result result = RestRequests.start( firstRunner, true );

        if ( !result.getStatus() ) {
            LOG.debug( "Could not get the result of start request" );
        }
        else {
            LOG.debug( "Result: " + result.getMessage() );
        }
    }


    @Test
    public void testStatus() {
        Map<String, Runner> runners = service.getRunners();

        for ( Runner runner : runners.values() ) {
            if ( runner.getHostname() != null ) {
                Result result = status( runner );
                LOG.debug( "Status result of runner {} = {}", runner.getHostname(), result );
            }
        }
    }
}