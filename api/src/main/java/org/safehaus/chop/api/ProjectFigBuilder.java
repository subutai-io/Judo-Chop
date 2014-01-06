package org.safehaus.chop.api;


import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Properties;

import org.safehaus.guicyfig.Bypass;
import org.safehaus.guicyfig.OptionState;
import org.safehaus.guicyfig.Overrides;

import org.apache.commons.lang.NotImplementedException;

import com.google.inject.Inject;


/**
 * Builds a ProjectFig for use many by the plugin.
 */
public class ProjectFigBuilder {
    private ProjectFig supplied;
    private String testPackageBase;
    private String createTimestamp;
    private String artifactId;
    private String version;
    private String groupId;
    private String vcsRepoUrl;
    private String commitId;
    private String loadKey;
    private String chopVersion;
    private String warMd5;
    private String loadTime;
    private String managerUsername;
    private String managerPassword;


    public ProjectFigBuilder() {
        // do nothing - supplied will be injected if in Guice env
    }


    public ProjectFigBuilder( ProjectFig project ) {
        // set the supplied project - this is manually provided
        this.supplied = project;
        updateValues();
    }


    @Inject
    public void setProject( ProjectFig project ) {
        if ( supplied == null ) {
            supplied = project;
            updateValues();
        }
    }


    private void updateValues() {
        if ( supplied == null ) {
            return;
        }

        this.testPackageBase = supplied.getTestPackageBase();
        this.createTimestamp = supplied.getCreateTimestamp();
        this.artifactId = supplied.getArtifactId();
        this.version = supplied.getVersion();
        this.groupId = supplied.getGroupId();
        this.vcsRepoUrl = supplied.getVcsRepoUrl();
        this.commitId = supplied.getVcsVersion();
        this.loadKey = supplied.getLoadKey();
        this.chopVersion = supplied.getChopVersion();
        this.warMd5 = supplied.getWarMd5();
        this.loadTime = supplied.getLoadTime();
        this.managerPassword = supplied.getManagerPassword();
        this.managerUsername = supplied.getManagerUsername();
    }


    public ProjectFigBuilder setTestPackageBase( final String testPackageBase ) {
        this.testPackageBase = testPackageBase;
        return this;
    }


    public ProjectFigBuilder setCreateTimestamp( final String timeStamp ) {
        this.createTimestamp = timeStamp;
        return this;
    }


    public ProjectFigBuilder setArtifactId( final String artifactId ) {
        this.artifactId = artifactId;
        return this;
    }


    public ProjectFigBuilder setProjectVersion( final String version ) {
        this.version = version;
        return this;
    }


    public ProjectFigBuilder setGroupId( final String groupId ) {
        this.groupId = groupId;
        return this;
    }


    public ProjectFigBuilder setVcsRepoUrl( final String vcsRepoUrl ) {
        this.vcsRepoUrl = vcsRepoUrl;
        return this;
    }


    public ProjectFigBuilder setVcsVersion( final String commitId ) {
        this.commitId = commitId;
        return this;
    }


    public ProjectFigBuilder setLoadKey( final String loadKey ) {
        this.loadKey = loadKey;
        return this;
    }


    public ProjectFigBuilder setChopVersion( final String version ) {
        this.chopVersion = version;
        return this;
    }


    public ProjectFigBuilder setWarMd5( final String warMd5 ) {
        this.warMd5 = warMd5;
        return this;
    }


    public ProjectFigBuilder setLoadTime( final String loadTime ) {
        this.loadTime = loadTime;
        return this;
    }


    public ProjectFigBuilder setManagerUsername( final String managerUsername ) {
        this.managerUsername = managerUsername;
        return this;
    }


    public ProjectFigBuilder setManagerPassword( final String managerPassword ) {
        this.managerPassword = managerPassword;
        return this;
    }


    public ProjectFig getProject() {
        return new ProjectFig() {
            @Override
            public String getChopVersion() {
                return chopVersion;
            }


            @Override
            public String getCreateTimestamp() {
                return createTimestamp;
            }


            @Override
            public String getVcsVersion() {
                return commitId;
            }


            @Override
            public String getVcsRepoUrl() {
                return vcsRepoUrl;
            }


            @Override
            public String getGroupId() {
                return groupId;
            }


            @Override
            public String getArtifactId() {
                return artifactId;
            }


            @Override
            public String getVersion() {
                return version;
            }


            @Override
            public String getTestPackageBase() {
                return testPackageBase;
            }


            @Override
            public long getTestStopTimeout() {
                return Long.parseLong( DEFAULT_TEST_STOP_TIMEOUT );
            }


            @Override
            public String getLoadTime() {
                return loadTime;
            }


            @Override
            public String getLoadKey() {
                return loadKey;
            }


            @Override
            public String getWarMd5() {
                return warMd5;
            }


            @Override
            public String getManagerUsername() {
                return managerUsername;
            }


            @Override
            public String getManagerPassword() {
                return managerPassword;
            }


            @Override
            public void addPropertyChangeListener( final PropertyChangeListener listener ) {
                throw new NotImplementedException();
            }


            @Override
            public void removePropertyChangeListener( final PropertyChangeListener listener ) {
                throw new NotImplementedException();
            }


            @Override
            public OptionState[] getOptions() {
                throw new NotImplementedException();
            }


            @Override
            public OptionState getOption( final String key ) {
                throw new NotImplementedException();
            }


            @Override
            public String getKeyByMethod( final String methodName ) {
                throw new NotImplementedException();
            }


            @Override
            public Object getValueByMethod( final String methodName ) {
                throw new NotImplementedException();
            }


            @Override
            public Properties filterOptions( final Properties properties ) {
                throw new NotImplementedException();
            }


            @Override
            public Map<String, Object> filterOptions( final Map<String, Object> entries ) {
                throw new NotImplementedException();
            }


            @Override
            public void override( final String key, final String override ) {
                throw new NotImplementedException();
            }


            @Override
            public void setOverrides( final Overrides overrides ) {
                throw new NotImplementedException();
            }


            @Override
            public Overrides getOverrides() {
                throw new NotImplementedException();
            }


            @Override
            public void bypass( final String key, final String bypass ) {
                throw new NotImplementedException();
            }


            @Override
            public void setBypass( final Bypass bypass ) {
                throw new NotImplementedException();
            }


            @Override
            public Bypass getBypass() {
                throw new NotImplementedException();
            }


            @Override
            public Class getFigInterface() {
                return ProjectFig.class;
            }


            @Override
            public boolean isSingleton() {
                return false;
            }
        };
    }
}
