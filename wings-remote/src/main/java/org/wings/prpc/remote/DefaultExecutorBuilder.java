package org.wings.prpc.remote;

import okhttp3.OkHttpClient;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.ConfigurationSource;
import org.stagemonitor.configuration.source.EnvironmentVariableConfigurationSource;
import org.stagemonitor.configuration.source.PropertyFileConfigurationSource;
import org.stagemonitor.configuration.source.SystemPropertyConfigurationSource;
import org.wings.prpc.remote.configuration.ExecutorConfiguration;
import org.wings.prpc.remote.configuration.PrefixingConfigurationSourceWrapper;

import java.util.ArrayList;
import java.util.List;

public class DefaultExecutorBuilder {

    private ConfigurationRegistry configurationRegistry;
    private OkHttpClient client;

    public DefaultExecutorBuilder withConfigurationRegistry(ConfigurationRegistry configurationRegistry) {
        this.configurationRegistry = configurationRegistry;
        return this;
    }

    public DefaultExecutorBuilder withClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public DefaultExecutor build() {
        if(configurationRegistry == null) {
            configurationRegistry = createDefaultConfigRegistry(getDefaultConfigSources());
        }
        if(client == null) {
            //getDefaultClient(config)
        }
        return new DefaultExecutor(client, configurationRegistry.getConfig(ExecutorConfiguration.class));
    }

    private List<ConfigurationSource> getDefaultConfigSources() {
        List<ConfigurationSource> sources = new ArrayList<>();
        sources.add(new PrefixingConfigurationSourceWrapper(new SystemPropertyConfigurationSource(), "wings.remote."));
        sources.add(new PrefixingConfigurationSourceWrapper(new EnvironmentVariableConfigurationSource(), "WINGS_REMOTE_"));
        if (PropertyFileConfigurationSource.isPresent("wings-remote.properties")) {
            sources.add(new PropertyFileConfigurationSource("wings-remote.properties"));
        }
        return sources;
    }

    private ConfigurationRegistry createDefaultConfigRegistry(List<ConfigurationSource> sources) {
        return ConfigurationRegistry.builder()
                .configSources(sources)
                .addOptionProvider(new ExecutorConfiguration())
                .build();
    }

    private OkHttpClient createDefaultClient() {
        return new OkHttpClient.Builder()
                .callTimeout()
                .build();
    }
}
