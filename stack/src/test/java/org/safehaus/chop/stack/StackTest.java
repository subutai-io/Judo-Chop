package org.safehaus.chop.stack;


import java.io.StringReader;
import java.net.URL;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


/**
 * This class tests stack serialization to and from JSON.
 */
public class StackTest {
    private static final Logger LOG = LoggerFactory.getLogger( StackTest.class );


    private UUID uuid = UUID.fromString( "adb51dfa-ed4f-4a36-9cbf-6b5a7b6da31e" );
    private BasicStack stack;


    @Before
    public void setup() {
        stack = new BasicStack();
    }


    @Test
    public void testBasicGeneration() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString( stack );
        LOG.debug( json );
        assertTrue( json.startsWith( "{\"name\":null,\"id\":\"" ) );
        assertTrue( json.endsWith( "\",\"clusters\":[]}" ) );
    }


    @Test
    public void testBasicName() throws Exception {
        stack.setName( "foobar" );
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString( stack );
        LOG.info( json );

        assertTrue( json.startsWith( "{\"name\":\"foobar\",\"id\":\"" ) );
        assertTrue( json.endsWith( "\",\"clusters\":[]}" ) );
    }


    @Test
    public void testCombo() throws Exception {
        stack.setName( "UG-2.0" )
             .setId( uuid )
             .add( new BasicCluster().setName( "ElasticSearch" ).setSize( 6 ).setInstanceSpec(
                     new BasicInstanceSpec().setKeyName( "TestKeyPair" ).setDataCenter( "us-east-1c" )
                                            .setImageId( "ami-c56152ac" )
                                            .setScriptEnvProperty( "ES_PATH", "/var/lib/elastic_search" )
                                            .setScriptEnvProperty( "JAVA_HOME", "/user/lib/jvm/default" )
                                            .setType( "m1.large" )
                                            .addSetupScript( new URL( "file://./install_es.sh" ) )
                                            .addSetupScript( new URL( "file://./setup_cassandra.sh" ) )
                                            .setRuleSetName( "UG-Chop-Rules" ).addInboundRule(
                             new BasicIpRule().withFromPort( 80 ).withToPort( 8080 ).withIpRanges( "0.0.0.0/32" )
                                              .withIpProtocol( "tcp" ) ).addInboundRule(
                             new BasicIpRule().withFromPort( 443 ).withToPort( 8443 ).withIpRanges( "0.0.0.0/32" )
                                              .withIpProtocol( "tcp" ) ) ) )
            .add( new BasicCluster().setName( "Cassandra" ).setSize( 6 ).setInstanceSpec(
                    new BasicInstanceSpec().setKeyName( "TestKeyPair" ).setDataCenter( "us-east-1c" )
                                           .setImageId( "ami-c56152ac" )
                                           .setScriptEnvProperty( "CASSANDRA_PATH", "/var/lib/cassandra" )
                                           .setScriptEnvProperty( "JAVA_HOME", "/user/lib/jvm/default" )
                                           .setType( "m1.xlarge" )
                                           .addSetupScript( new URL( "file://./install_cassandra.sh" ) )
                                           .addSetupScript( new URL( "file://./setup_cassandra.sh" ) )
                                           .setRuleSetName( "UG-Chop-Rules" ).addInboundRule(
                            new BasicIpRule().withFromPort( 80 ).withToPort( 8080 ).withIpRanges( "0.0.0.0/32" )
                                             .withIpProtocol( "tcp" ) ).addInboundRule(
                            new BasicIpRule().withFromPort( 443 ).withToPort( 8443 ).withIpRanges( "0.0.0.0/32" )
                                             .withIpProtocol( "tcp" ) ) ) );

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString( stack );
        LOG.info( json );
        LOG.info( "--------------" );

        BasicStack foo = mapper.readValue( new StringReader( json ), BasicStack.class );
        assertEquals( "UG-2.0", foo.getName() );
        assertEquals( uuid.toString(), foo.getId().toString() );
        assertNotNull( foo.getClusters().get( 0 ) );
        assertEquals( "ElasticSearch", foo.getClusters().get( 0 ).getName() );

        Cluster cluster = foo.getClusters().get( 0 );
        InstanceSpec spec = cluster.getInstanceSpec();
        assertEquals( "file://./install_es.sh", spec.getSetupScripts().get( 0 ).toString() );
//        URL script = spec.getSetupScripts().get( 0 );
        URL script = new URL( "file://./install_es.sh" );
        LOG.info( "setup script path = {}", script.getPath() );

        LOG.info( "script modified = {}", script.getPath().substring( 1 ) );
        URL reloaded = getClass().getClassLoader().getResource( script.getPath().substring( 1 ) );
        LOG.info( "reloaded form CL script path = {}", reloaded.toString() );
    }
}

