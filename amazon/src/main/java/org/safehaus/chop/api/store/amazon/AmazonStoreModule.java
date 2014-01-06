/*
 * Created by IntelliJ IDEA.
 * User: akarasulu
 * Date: 12/8/13
 * Time: 5:40 PM
 */
package org.safehaus.chop.api.store.amazon;


import org.safehaus.chop.api.Constants;
import org.safehaus.chop.api.StoreService;
import org.safehaus.guicyfig.GuicyFigModule;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;


public class AmazonStoreModule extends AbstractModule implements Constants {


    protected void configure() {
        install( new GuicyFigModule( AmazonFig.class ) );
        bind( StoreService.class ).to( AmazonS3ServiceAwsImpl.class );
    }


    @Inject
    @Provides
    AmazonS3Client provideAmazonS3Client( final AmazonFig amazonFig ) {
        AmazonS3Client client;

        AWSCredentials credentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return amazonFig.getAwsKey();
            }


            @Override
            public String getAWSSecretKey() {
                return amazonFig.getAwsSecret();
            }
        };

        client = new AmazonS3Client( credentials );
        return client;
    }
}
