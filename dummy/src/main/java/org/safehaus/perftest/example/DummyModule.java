/*
 * Created by IntelliJ IDEA.
 * User: akarasulu
 * Date: 12/6/13
 * Time: 1:10 AM
 */
package org.safehaus.perftest.example;


import org.safehaus.perftest.Perftest;

import com.google.inject.AbstractModule;


public class DummyModule extends AbstractModule {
    protected void configure() {
        bind( Perftest.class ).to( DummyPerftest.class );
    }
}