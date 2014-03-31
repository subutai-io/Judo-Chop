package org.safehaus.chop.runner;


import org.junit.ClassRule;
import org.junit.Test;
import org.safehaus.chop.api.Runner;
import org.safehaus.jettyjam.utils.ContextListener;
import org.safehaus.jettyjam.utils.FilterMapping;
import org.safehaus.jettyjam.utils.HttpsConnector;
import org.safehaus.jettyjam.utils.JettyConnectors;
import org.safehaus.jettyjam.utils.JettyContext;
import org.safehaus.jettyjam.utils.JettyResource;
import org.safehaus.jettyjam.utils.JettyUnitResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;


/**
 * Tests the Runner.
 */
public class RunnerAppTest {
    private static final Logger LOG = LoggerFactory.getLogger( RunnerAppTest.class );

    @JettyContext(
        enableSession = true,
        contextListeners = { @ContextListener( listener = RunnerConfig.class ) },
        filterMappings = { @FilterMapping( filter = GuiceFilter.class, spec = "/*") }
    )
    @JettyConnectors(
        defaultId = "https",
        httpsConnectors = { @HttpsConnector( id = "https", port = Runner.DEFAULT_SERVER_PORT_INT ) }
    )
    @ClassRule
    public static JettyResource jetty = new JettyUnitResource();


    @Test
    public void testStart() {
        RunnerTestUtils.testStart( jetty.newTestParams().setLogger( LOG ) );
    }


    @Test
    public void testReset() {
        RunnerTestUtils.testReset( jetty.newTestParams().setLogger( LOG ) );
    }
}

