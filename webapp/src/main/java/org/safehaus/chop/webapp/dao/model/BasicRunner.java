package org.safehaus.chop.webapp.dao.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.safehaus.chop.api.Runner;
import org.safehaus.guicyfig.Bypass;
import org.safehaus.guicyfig.OptionState;
import org.safehaus.guicyfig.Overrides;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Properties;

public class BasicRunner implements Runner {

    private String ipv4Address;
    private String hostname;
    private int serverPort;
    private String url;
    private String tempDir;

    public BasicRunner(String ipv4Address, String hostname, int serverPort, String url, String tempDir) {
        this.ipv4Address = ipv4Address;
        this.hostname = hostname;
        this.serverPort = serverPort;
        this.url = url;
        this.tempDir = tempDir;
    }

    @Override
    public String getIpv4Address() {
        return ipv4Address;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getTempDir() {
        return tempDir;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ipv4Address", ipv4Address)
                .append("hostname", hostname)
                .append("serverPort", serverPort)
                .append("url", url)
                .append("tempDir", tempDir)
                .toString();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        
    }

    @Override
    public OptionState[] getOptions() {
        return new OptionState[0];  
    }

    @Override
    public OptionState getOption(String s) {
        return null;  
    }

    @Override
    public String getKeyByMethod(String s) {
        return null;  
    }

    @Override
    public Object getValueByMethod(String s) {
        return null;  
    }

    @Override
    public Properties filterOptions(Properties properties) {
        return null;  
    }

    @Override
    public Map<String, Object> filterOptions(Map<String, Object> stringObjectMap) {
        return null;  
    }

    @Override
    public void override(String s, String s2) {
        
    }

    @Override
    public boolean setOverrides(Overrides overrides) {
        return false;  
    }

    @Override
    public Overrides getOverrides() {
        return null;  
    }

    @Override
    public void bypass(String s, String s2) {
        
    }

    @Override
    public boolean setBypass(Bypass bypass) {
        return false;  
    }

    @Override
    public Bypass getBypass() {
        return null;  
    }

    @Override
    public Class getFigInterface() {
        return null;  
    }

    @Override
    public boolean isSingleton() {
        return false;  
    }
}