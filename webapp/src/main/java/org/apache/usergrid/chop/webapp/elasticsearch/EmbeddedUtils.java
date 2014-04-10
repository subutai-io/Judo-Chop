package org.apache.usergrid.chop.webapp.elasticsearch;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.internal.InternalNode;
import org.elasticsearch.transport.netty.NettyTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.usergrid.chop.webapp.elasticsearch.ElasticSearchFig.DATA_DIR_KEY;


/**
 * Utility functions for dealing with embedded elasticsearch instances.
 */
public class EmbeddedUtils {
    private static final Logger LOG = LoggerFactory.getLogger( EmbeddedUtils.class );


    public static void extractTransportInfo( InternalNode inode, ElasticSearchFig config ) {
        TransportAddress ta = getTransportAddress( inode );
        LOG.info( "ta = {}", ta.toString() );

        String[] strings = ta.toString().split( ":" );
        String transportHost = strings[0].substring( 6 );
        String transportPortStr = strings[1].substring( 0, strings[1].length() - 1 );
        LOG.info( "transport host = {}, transport port = {}", transportHost, transportPortStr );

        config.bypass( ElasticSearchFig.PORT_KEY, transportPortStr );
        config.bypass( ElasticSearchFig.SERVERS_KEY, transportHost );
    }


    public static TransportAddress getTransportAddress( InternalNode inode ) {
        return inode.injector().getInstance( NettyTransport.class ).boundAddress().publishAddress();
    }


    public static InternalNode newInstance( ElasticSearchFig config ) {
        InternalNode inode = ( InternalNode )
                NodeBuilder.nodeBuilder().settings( getSettings( config ) )
                           .data( true )
                           .clusterName( config.getClusterName() )
                           .node();

        extractTransportInfo( inode, config );
        return inode;
    }


    public static Settings getSettings( ElasticSearchFig config ) {
        String dataDir;
        InputStream in = EmbeddedUtils.class.getClassLoader().getResourceAsStream( "EmbeddedUtils.properties" );

        if ( in != null ) {
            Properties props = new Properties();


            try {
                props.load( in );
            }
            catch ( IOException e ) {
                LOG.warn( "Failed to load properties file from EmbeddedUtils.properties" );
                dataDir = config.getDataDir();
            }

            dataDir = props.getProperty( DATA_DIR_KEY );

            if ( ! dataDir.equals( config.getDataDir() ) ) {
                config.bypass( DATA_DIR_KEY, dataDir );
            }

            try {
                in.close();
            }
            catch ( IOException e ) {
                LOG.warn( "Failure while trying to close stream" );
            }
        }
        else {
            dataDir = config.getDataDir();
        }

        return ImmutableSettings.settingsBuilder()
                .put( "path.data", dataDir )
                .build();
    }
}
