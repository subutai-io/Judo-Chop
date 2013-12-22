package org.safehaus.chop.api.store.amazon;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.safehaus.chop.api.ApiModule;
import org.safehaus.chop.api.Runner;
import org.safehaus.chop.api.store.StoreOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.google.inject.Guice;


/** Tests the S3Operations */
public class S3OperationsTest {
    private static final Logger LOG = LoggerFactory.getLogger( S3Operations.class );
    StoreOperations operations =
            Guice.createInjector( new AmazonStoreModule(), new ApiModule() ).getInstance( S3Operations.class );

    private String accessKey = System.getProperty( "accessKey" );
    private String secretKey = System.getProperty( "secretKey" );
    private String amiID = System.getProperty( "amiID" );
    private String securityGroup = System.getProperty( "securityGroup" );
    private String keyName = System.getProperty( "keyName" );


    @Test
    public void testRunnersListing() {
        Map<String, Runner> runners = operations.getRunners( new Ec2Runner() );

        for ( Runner runner : runners.values() ) {
            LOG.debug( "Got runner {}", runner );
        }
    }


    @Test
    public void testRegister() {
        Ec2Runner metadata = new Ec2Runner();
        metadata.setProperty( "foo", "bar" );
        metadata.setProperty( ConfigKeys.HOSTNAME_KEY, "foobar-host" );
        operations.register( metadata );
    }


    @Test
    public void testDeleteGhostRunners() {
        final String runnerName = "chop-runner";
        EC2Manager ec2 = new EC2Manager( accessKey, secretKey, amiID, securityGroup, keyName, runnerName );
        Collection<Instance> instances = ec2.getInstances( runnerName, InstanceStateName.Running );
        Collection<String> instanceHosts = new ArrayList<String>( instances.size() );
        for ( Instance i : instances ) {
            instanceHosts.add( i.getPublicDnsName() );
        }
        operations.deleteGhostRunners( instanceHosts );

    }
}