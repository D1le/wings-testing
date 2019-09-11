package org.wings.prpc.remote;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.ConfigurationSource;
import org.stagemonitor.configuration.source.EnvironmentVariableConfigurationSource;
import org.stagemonitor.configuration.source.PropertyFileConfigurationSource;
import org.stagemonitor.configuration.source.SystemPropertyConfigurationSource;
import org.wings.prpc.remote.configuration.ExecutorConfiguration;
import org.wings.prpc.remote.configuration.source.PrefixingConfigurationSourceWrapper;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
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
        if (configurationRegistry == null) {
            configurationRegistry = createDefaultConfigRegistry(getDefaultConfigSources());
        }
        if (client == null) {
            client = createDefaultClient(getExecutorConfig(configurationRegistry));
        }
        return new DefaultExecutor(client, getExecutorConfig(configurationRegistry));
    }

    private ExecutorConfiguration getExecutorConfig(ConfigurationRegistry registry) {
        return configurationRegistry.getConfig(ExecutorConfiguration.class);
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

    private OkHttpClient createDefaultClient(ExecutorConfiguration config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .callTimeout(configuration.getTimeout().getMillis(), TimeUnit.MILLISECONDS)
                .callTimeout(Duration.ofMillis(config.getTimeout().getMillis()))
                .connectTimeout(Duration.ZERO)
                .readTimeout(Duration.ZERO)
                .writeTimeout(Duration.ZERO)
                .authenticator(createBasicAuthenticator(config));
        if (!config.isVerifyServerCert()) {
            X509TrustManager trustManager = createTrustAllTrustManager();
            builder.sslSocketFactory(createTrustAllSocketFactory(trustManager), trustManager);
            builder.hostnameVerifier((hostname, session) -> true);
        }
        return builder.build();
    }

    @NotNull
    private Authenticator createBasicAuthenticator(ExecutorConfiguration configuration) {
        return (route, response) -> {
            if (response.request().header("Authorization") != null) {
                return null;
            }
            String username = configuration.getUsername().orElse(null);
            String password = configuration.getPassword().orElse(null);
            if (username != null && password != null) {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header("Authorization", credential)
                        .build();
            }
            return null;
        };
    }

    private SSLSocketFactory createTrustAllSocketFactory(X509TrustManager trustManager) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            throw new RuntimeException("Failed to initialize SSL context", ex);
        }
        return sslContext.getSocketFactory();
    }

    private X509TrustManager createTrustAllTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };
    }
}