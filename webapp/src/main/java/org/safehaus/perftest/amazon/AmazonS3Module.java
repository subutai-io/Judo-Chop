package org.safehaus.perftest.amazon;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.safehaus.perftest.RunnerInfo;
import org.safehaus.perftest.settings.PropSettings;
import org.safehaus.perftest.settings.Props;


public class AmazonS3Module extends AbstractModule implements Props {
    private AmazonS3Client client;


    protected void configure() {
        bind( RunnerInfo.class ).to( Ec2RunnerInfo.class );
        bind( S3Operations.class );
        bind( AmazonS3Service.class ).to( AmazonS3ServiceAwsImpl.class );
    }


    @Provides
    AmazonS3Client provideAmazonS3Client() {
        if ( client != null )
        {
            return client;
        }

        AWSCredentials credentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return PropSettings.getAwsKey();
            }

            @Override
            public String getAWSSecretKey() {
                return PropSettings.getAwsSecret();
            }
        };

        client = new AmazonS3Client( credentials );
        return client;
    }
}