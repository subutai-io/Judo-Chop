package org.safehaus.perftest.plugin;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;


@Mojo ( name = "start" )
public class PerftestStartMojo extends PerftestMojo {

    @Override
    public void execute() throws MojoExecutionException {

    }
}