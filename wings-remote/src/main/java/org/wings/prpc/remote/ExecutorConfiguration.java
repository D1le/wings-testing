package org.wings.prpc.remote;

public interface ExecutorConfiguration {

    public static final String DEFAULT_SCHEME_PROPERTY_NAME = "scheme";
    public static final String DEFAULT_HOST_PROPERTY_NAME = "host";
    public static final String DEFAULT_PORT_PROPERTY_NAME = "port";
    public static final String DEFAULT_USERNAME_PROPERTY_NAME = "username";
    public static final String DEFAULT_PASSWORD_PROPERTY_NAME = "password";
    public static final String DEFAULT_RESTPATH_PROPERTY_NAME = "restpath";
    public static final String DEFAULT_TIMEOUT_PROPERTY_NAME = "timeout";

    String getScheme();

    String getHost();

    String getPort();

    String getUsername();

    String getPassword();

    String getRestPath();

    int getTimeout();

}
