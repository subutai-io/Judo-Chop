/*
 * Created by IntelliJ IDEA.
 * User: akarasulu
 * Date: 11/22/13
 * Time: 11:44 PM
 */
package org.safehaus.chop.webapp;


import java.util.HashMap;
import java.util.Map;

import org.safehaus.chop.client.ChopClientModule;
import org.safehaus.guicyfig.GuicyFigModule;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;


public class Module extends ServletModule {
    public static final String PACKAGES_KEY = "com.sun.jersey.config.property.packages";


    protected void configureServlets() {
        install( new GuicyFigModule( ServletFig.class ) );
        install( new ChopClientModule() );

        // Hook Jersey into Guice Servlet
        bind( GuiceContainer.class );

        // Hook Jackson into Jersey as the POJO <-> JSON mapper
        bind( JacksonJsonProvider.class ).asEagerSingleton();

        Map<String, String> params = new HashMap<String, String>();
        params.put( PACKAGES_KEY, getClass().getPackage().toString() );
        serve( "/*" ).with( GuiceContainer.class, params );
    }
}
