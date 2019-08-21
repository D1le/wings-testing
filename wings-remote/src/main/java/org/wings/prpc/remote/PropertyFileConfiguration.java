package org.wings.prpc.remote;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileConfiguration implements ExecutorConfiguration {

    private Properties properties;

    public PropertyFileConfiguration(String location) {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(location);
        try {
            properties = new Properties();
            properties.load(resource);
        } catch (IOException ex) {

        }
    }

    @Override
    public String getScheme() {
        return properties.getProperty(DEFAULT_SCHEME_PROPERTY_NAME);
    }

    @Override
    public String getHost() {
        return properties.getProperty(DEFAULT_HOST_PROPERTY_NAME);
    }

    @Override
    public String getPort() {
        return properties.getProperty(DEFAULT_PORT_PROPERTY_NAME);
    }

    @Override
    public String getUsername() {
        return properties.getProperty(DEFAULT_USERNAME_PROPERTY_NAME);
    }

    @Override
    public String getPassword() {
        return properties.getProperty(DEFAULT_PASSWORD_PROPERTY_NAME);
    }

    @Override
    public String getRestPath() {
        return properties.getProperty(DEFAULT_RESTPATH_PROPERTY_NAME);
    }

    @Override
    public int getTimeout() {
        return Integer.parseInt(properties.getProperty(DEFAULT_TIMEOUT_PROPERTY_NAME));
    }
}
