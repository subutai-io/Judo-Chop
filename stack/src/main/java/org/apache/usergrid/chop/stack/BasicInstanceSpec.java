package org.apache.usergrid.chop.stack;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * A basic implementation of a InstanceSpec.
 */
public class BasicInstanceSpec implements InstanceSpec
{
    private String imageId;
    private String type;
    private String keyName;
    private List<URL> setupScripts = new ArrayList<URL>();
    private Properties scriptEnvironment = new Properties();



    @Override
    public String getImageId() {
        return imageId;
    }


    public BasicInstanceSpec setImageId( String imageId ) {
        this.imageId = imageId;
        return this;
    }


    @Override
    public String getType() {
        return type;
    }


    public BasicInstanceSpec setType( final String type ) {
        this.type = type;
        return this;
    }


    @Override
    public String getKeyName() {
        return keyName;
    }


    public BasicInstanceSpec setKeyName( final String keyName ) {
        this.keyName = keyName;
        return this;
    }


    @Override
    public List<URL> getSetupScripts() {
        return setupScripts;
    }


    public BasicInstanceSpec setSetupScripts( final List<URL> setupScripts ) {
        this.setupScripts = setupScripts;
        return this;
    }


    public BasicInstanceSpec addSetupScript( URL setupScript ) {
        this.setupScripts.add( setupScript );
        return this;
    }


    @Override
    public Properties getScriptEnvironment() {
        return scriptEnvironment;
    }


    public BasicInstanceSpec setScriptEnvironment( final Properties scriptEnvironment ) {
        this.scriptEnvironment = scriptEnvironment;
        return this;
    }


    public BasicInstanceSpec setScriptEnvProperty( String key, String value ) {
        this.scriptEnvironment.setProperty( key, value );
        return this;
    }
}
