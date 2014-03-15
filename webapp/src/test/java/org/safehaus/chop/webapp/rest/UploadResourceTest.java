package org.safehaus.chop.webapp.rest;


import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.junit.ClassRule;
import org.junit.Test;
import org.safehaus.chop.webapp.ChopUiConfig;
import org.safehaus.embedded.jetty.utils.ContextListener;
import org.safehaus.embedded.jetty.utils.FilterMapping;
import org.safehaus.embedded.jetty.utils.HttpsConnector;
import org.safehaus.embedded.jetty.utils.JettyConnectors;
import org.safehaus.embedded.jetty.utils.JettyContext;
import org.safehaus.embedded.jetty.utils.JettyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;


/** Tests the UploadResource REST web service. */
public class UploadResourceTest {
    private final static Logger LOG = LoggerFactory.getLogger( UploadResourceTest.class );

    @JettyContext(
        enableSession = true,
        contextListeners = { @ContextListener( listener = ChopUiConfig.class ) },
        filterMappings = { @FilterMapping( filter = GuiceFilter.class, spec = "/*") }
    )
    @JettyConnectors(
        defaultId = "https",
        httpsConnectors = { @HttpsConnector( id = "https", port = 8443 ) }
    )
    @ClassRule
    public static JettyResource jetty = new JettyResource();


    @Test
    public void testGet() {
        WebResource resource = Client.create().resource( jetty.getServerUrl().toString() + TestGetResource.ENDPOINT_URL );
        String result = resource.type( MediaType.TEXT_PLAIN_TYPE ).get( String.class );
    }


    @Test
    public void testUpload() {
        InputStream in = getClass().getClassLoader().getResourceAsStream( "test.war" );

        FormDataMultiPart part = new FormDataMultiPart();
        part.field( UploadResource.FILENAME_PARAM, "test.war" );

        FormDataBodyPart body = new FormDataBodyPart( UploadResource.CONTENT,
                in, MediaType.APPLICATION_OCTET_STREAM_TYPE );
        part.bodyPart( body );

        LOG.debug( "Server URL = {}", jetty.getServerUrl().toString() );

        WebResource resource = Client.create().resource( jetty.getServerUrl().toString() + UploadResource.ENDPOINT_URL );
        String result = resource.type( MediaType.MULTIPART_FORM_DATA_TYPE ).post( String.class, part );

        LOG.debug( "Got back result = {}", result );
    }
}