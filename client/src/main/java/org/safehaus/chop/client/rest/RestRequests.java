package org.safehaus.chop.client.rest;


import javax.ws.rs.core.MediaType;

import org.safehaus.chop.api.BaseResult;
import org.safehaus.chop.api.ConfigKeys;
import org.safehaus.chop.api.PropagatedResult;
import org.safehaus.chop.api.Result;
import org.safehaus.chop.api.Runner;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import static org.safehaus.chop.api.ConfigKeys.PARAM_PROJECT;
import static org.safehaus.chop.api.ConfigKeys.PARAM_PROPAGATE;


/**
 * Client REST request functions.
 */
public class RestRequests {
    /**
     * Performs a POST HTTP operation against the /load endpoint with the perftest query parameter, and propagate query
     * parameter.
     *
     * @param runner the runner to perform the load operation on
     * @param perftest the perftest query parameter value
     * @param propagate whether or not to enable propagation
     *
     * @return the result of the operation
     */
    public static Result load( Runner runner, String perftest, Boolean propagate ) {
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create( clientConfig );
        WebResource resource = client.resource( runner.getUrl() ).path( "/load" );
        return resource.queryParam( PARAM_PROPAGATE, propagate.toString() ).queryParam( PARAM_PROJECT, perftest )
                       .accept( MediaType.APPLICATION_JSON_TYPE ).post( PropagatedResult.class );
    }


    /**
     * Performs a POST HTTP operation against the /start endpoint with a propagate query parameter.
     *
     * @param runner the runner which will perform the start operation
     * @param propagate whether or not to enable propagation
     *
     * @return the result of the operation
     */
    public static Result start( Runner runner, Boolean propagate ) {
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create( clientConfig );
        WebResource resource = client.resource( runner.getUrl() ).path( "/start" );
        return resource.queryParam( PARAM_PROPAGATE, propagate.toString() ).accept( MediaType.APPLICATION_JSON_TYPE )
                       .post( PropagatedResult.class );
    }


    /**
     * Performs a POST HTTP operation against the /reset endpoint with a propagate query parameter.
     *
     * @param runner the runner to perform the reset operation on
     * @param propagate whether or not to enable propagation
     *
     * @return the result of the operation
     */
    public static Result reset( Runner runner, Boolean propagate ) {
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create( clientConfig );
        WebResource resource = client.resource( runner.getUrl() ).path( "/reset" );
        return resource.queryParam( PARAM_PROPAGATE, propagate.toString() ).accept( MediaType.APPLICATION_JSON_TYPE )
                       .post( PropagatedResult.class );
    }


    /**
     * Performs a POST HTTP operation against the /stop endpoint with a propagate query parameter.
     *
     * @param runner the runner which will perform the stop operation
     * @param propagate whether or not to enable propagation
     *
     * @return the result of the operation
     */
    public static Result stop( Runner runner, Boolean propagate ) {
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create( clientConfig );
        WebResource resource = client.resource( runner.getUrl() ).path( "/stop" );
        return resource.queryParam( PARAM_PROPAGATE, propagate.toString() ).accept( MediaType.APPLICATION_JSON_TYPE )
                       .post( PropagatedResult.class );
    }


    /**
     * Performs a GET HTTP operation against the /status endpoint.
     *
     * @param runner the runner to perform the status operation on
     *
     * @return the result of the operation
     */
    public static Result status( Runner runner ) {
        DefaultClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create( clientConfig );
        WebResource resource = client.resource( runner.getUrl() ).path( "/status" );
        return resource.accept( MediaType.APPLICATION_JSON_TYPE ).get( BaseResult.class );
    }
}