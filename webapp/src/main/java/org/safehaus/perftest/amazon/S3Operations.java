package org.safehaus.perftest.amazon;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.safehaus.perftest.settings.PropSettings;
import org.safehaus.perftest.settings.RunInfo;
import org.safehaus.perftest.settings.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


/**
 * Used to encapsulate the various S3 operations to perform.
 */
public class S3Operations {
    private static final Logger LOG = LoggerFactory.getLogger( S3Operations.class );
    private final AmazonS3Client client;


    @Inject
    public S3Operations( AmazonS3Client client ) {
        this.client = client;
    }


    /**
     * Registers this runner's instance by adding it's instance information into
     * S3 as a properties file into the bucket using the following key format:
     *
     *      "runners/formationName-publicHostname.properties"
     *
     * @param metadata the runner's instance metadata to be registered
     */
    public void register( Ec2Metadata metadata ) {
        StringBuilder sb = new StringBuilder();
        sb.append( PropSettings.getFormation() ).append('-')
          .append( metadata.getPublicHostname() ).append( ".properties" );
        String blobName = sb.toString();

        try {
            PutObjectRequest putRequest = new PutObjectRequest( PropSettings.getBucket(),
                    PropSettings.getRunners() + "/" + blobName,
                    metadata.getPropertiesAsStream(), new ObjectMetadata() );
            client.putObject( putRequest );
        }
        catch ( IOException e ) {
            LOG.error( "Failed to create input stream for object.", e );
        }

        LOG.info( "Successfully registered {}", blobName );
    }


    public Set<String> getTests() {
        Set<String> tests = new HashSet<String>();
        ObjectListing listing = client.listObjects( PropSettings.getBucket(), PropSettings.getTests() + "/" );

        do {
            for ( S3ObjectSummary summary : listing.getObjectSummaries() ) {
                if ( summary.getKey().equals( PropSettings.getTests() + "/" ) )
                {
                    continue;
                }

                tests.add( summary.getKey() );
            }

            listing = client.listNextBatchOfObjects( listing );
        }
        while ( listing.isTruncated() );

        return tests;
    }


    public Map<String,Ec2Metadata> getRunners() {
        Map<String,Ec2Metadata> runners = new HashMap<String, Ec2Metadata>();
        ObjectListing listing = client.listObjects( PropSettings.getBucket(),
                PropSettings.getRunners() + "/" + PropSettings.getFormation() );

        do {
            for ( S3ObjectSummary summary : listing.getObjectSummaries() ) {
                String key = summary.getKey();

                LOG.debug( "Got key {} while scanning under runners container", key );

                S3Object s3Object = client.getObject( PropSettings.getBucket(), key );

                try {
                    runners.put( key, new Ec2Metadata( s3Object.getObjectContent() ) );
                }
                catch ( IOException e ) {
                    LOG.error( "Failed to load metadata for runner {}", key, e );
                }
            }

            listing = client.listNextBatchOfObjects( listing );
        }
        while ( listing.isTruncated() );

        return runners;
    }


    public File download( File tempDir, String perftest ) throws IOException, InterruptedException {
        File tempFile = File.createTempFile( "perftest", "war", tempDir );
        LOG.debug( "Created temporary file {} for new war download.", tempFile.getAbsolutePath() );

        S3Object s3Object = client.getObject( PropSettings.getBucket(), perftest );
        LOG.debug( "Got S3Object:\n{}", s3Object.toString() );

        // Download war file contents into temporary file
        S3ObjectInputStream in = s3Object.getObjectContent();
        FileOutputStream out = new FileOutputStream( tempFile );
        byte[] buffer = new byte[1024];
        int readAmount;

        while ( ( readAmount = in.read( buffer ) ) != -1 ) {
            out.write( buffer, 0, readAmount );
        }

        out.flush();
        out.close();
        in.close();
        LOG.info( "Successfully downloaded {} from S3 to {}.", perftest, tempFile.getAbsoluteFile() );
        return tempFile;
    }


    public void uploadRunInfo( TestInfo testInfo, RunInfo runInfo ) {
        String loadKey = testInfo.getLoadKey();
        loadKey = loadKey.substring( 0, loadKey.length() - "perftest.war".length() );

        StringBuilder sb = new StringBuilder();
        sb.append( loadKey ).append( '/' )
                .append( "results/" )
                .append( runInfo.getRunNumber() )
                .append( "/run-info.json" );

        String blobName = sb.toString();
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayInputStream in = null;

        try {
            byte[] json = mapper.writeValueAsBytes(testInfo);
            in = new ByteArrayInputStream( json );
        }
        catch ( JsonProcessingException e ) {
            LOG.error("Failed to serialize to JSON TestInfo object {}", testInfo, e);
        }

        PutObjectRequest putRequest = new PutObjectRequest( PropSettings.getBucket(),
                blobName, in, new ObjectMetadata() );
        client.putObject( putRequest );

        LOG.info( "Successfully registered {}", blobName );
    }


    public void uploadResults( Ec2Metadata metadata, TestInfo testInfo, RunInfo runInfo, File results ) {
        uploadRunInfo( testInfo, runInfo );

        String loadKey = testInfo.getLoadKey();
        loadKey = loadKey.substring( 0, loadKey.length() - "perftest.war".length() );

        StringBuilder sb = new StringBuilder();
        sb.append( loadKey )
                .append( "results/" )
                .append( runInfo.getRunNumber() )
                .append( '/' )
                .append( metadata.getPublicHostname() )
                .append( "-results.log" );

        String blobName = sb.toString();

        PutObjectRequest putRequest = null;
        try {
            putRequest = new PutObjectRequest( PropSettings.getBucket(),
                    blobName, new FileInputStream( results ), new ObjectMetadata() );
        }
        catch ( FileNotFoundException e ) {
            LOG.error( "Failed to upload the results {} file", results, e );
        }
        client.putObject( putRequest );

        LOG.info( "Successfully registered {}", blobName );
    }


    private boolean hasKey( String key ) {
        ObjectListing listing = client.listObjects( PropSettings.getBucket(), key );

        if ( listing.getObjectSummaries().isEmpty() ) {
            return false;
        }

        S3ObjectSummary summary = listing.getObjectSummaries().get( 0 );
        LOG.debug( "Got key {} while scanning for key {}", summary.getKey() );
        return true;
    }


    public void uploadTestInfo( TestInfo testInfo ) {
        String loadKey = testInfo.getLoadKey();
        loadKey = loadKey.substring( 0, loadKey.length() - "perftest.war".length() );

        StringBuilder sb = new StringBuilder();
        sb.append( loadKey ).append('/')
                .append( "results/" )
                .append( "test-info.json" );

        if ( hasKey( sb.toString() ) ) {
            LOG.warn( "The key {} already exists for TestInfo - not updating!", sb.toString() );
            return;
        }

        String blobName = sb.toString();
        ObjectMapper mapper = new ObjectMapper();
        ByteArrayInputStream in = null;

        try {
            byte[] json = mapper.writeValueAsBytes(testInfo);
            in = new ByteArrayInputStream( json );
        }
        catch ( JsonProcessingException e ) {
            LOG.error("Failed to serialize to JSON TestInfo object {}", testInfo, e);
        }

        PutObjectRequest putRequest = new PutObjectRequest( PropSettings.getBucket(),
                blobName, in, new ObjectMetadata() );
        client.putObject( putRequest );

        LOG.info( "Successfully saved TestInfo with key {}", blobName );
    }
}
