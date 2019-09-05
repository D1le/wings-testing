package org.wings.prpc.remote.configuration;

import org.stagemonitor.configuration.ConfigurationOption;
import org.stagemonitor.configuration.ConfigurationOptionProvider;

import java.net.URL;
import java.util.Optional;

public class ExecutorConfiguration extends ConfigurationOptionProvider {

    private ConfigurationOption<String> scheme = ConfigurationOption.stringOption()
            .key("scheme")
            .addValidOptions("http", "https")
            .buildWithDefault("http");

    private ConfigurationOption<String> host = ConfigurationOption.stringOption()
            .key("host")
            .buildWithDefault("localhost");

    private ConfigurationOption<Integer> port = ConfigurationOption.integerOption()
            .key("port")
            .buildWithDefault(8080);

    private ConfigurationOption<Optional<String>> username = ConfigurationOption.stringOption()
            .key("username")
            .buildOptional();

    private ConfigurationOption<Optional<String>> password = ConfigurationOption.stringOption()
            .key("password")
            .sensitive()
            .buildOptional();

    private ConfigurationOption<String> restPath = ConfigurationOption.stringOption()
            .key("restpath")
            .buildWithDefault("/prweb/PRRestService");

    private ConfigurationOption<Integer> timeout = ConfigurationOption.integerOption()
            .key("timeout")
            .buildWithDefault(30);

    private ConfigurationOption<String> servicePath = ConfigurationOption.stringOption()
            .key("servicepath")
            .buildWithDefault("/mon/v1/test");

    public String getScheme() {
        return scheme.get();
    }

    public String getHost() {
        return host.get();
    }

    public int getPort() {
        return port.get();
    }

    public Optional<String> getUsername() {
        return username.get();
    }

    public Optional<String> getPassword() {
        return password.get();
    }

    public String getRestPath() {
        return restPath.get();
    }

    public int getTimeout() {
        return timeout.get();
    }

    public String getServicePath() {
        return servicePath.get();
    }

    public URL getExecutorEndpoint() {
//        new HttpUrl.Builder()
//                .
        try {
            return new URL(getScheme(), getHost(), getPort(), getRestPath() + getServicePath());
        } catch (Exception ex) {

        }
        return null;
    }

}
