package org.wings.prpc.remote;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Test;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.SimpleSource;
import org.wings.prpc.remote.configuration.ExecutorConfiguration;
import org.wings.prpc.remote.dependency.ClassDependency;
import org.wings.prpc.remote.dependency.ClassDependencyResolver;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DefaultExecutorTest extends ExecutorTestSupport {

    @Test
    void happyPath() throws Exception {
        server.setDispatcher(DEFAULT_DISPATCHER);
        server.start();

        HttpUrl url = server.url("");

        SimpleSource source = new SimpleSource();
        source.add("port", String.valueOf(url.port()));

        Executor executor = new DefaultExecutor(new OkHttpClient(), getConfiguration(source));

        ActionContainer<String> ac = new ActionContainer<>();
        ac.setAction((Action<String>) () -> "some_response");

        Future<Output<String>> execute = executor.execute(ac);
        Output<String> output = execute.get();

        System.out.println(output);
    }

    @Test
    void timeout() throws Exception {
        server.enqueue(new MockResponse().throttleBody(1, 200, TimeUnit.MILLISECONDS));
        server.start();

        HttpUrl url = server.url("");

        SimpleSource source = new SimpleSource();
        source.add("port", String.valueOf(url.port()));
        source.add("timeout", "100ms");

        Executor executor = new DefaultExecutorBuilder()
                .withConfigurationRegistry(getRegistry(source))
                .build();

        ActionContainer<String> ac = new ActionContainer<>();
        ac.setAction((Action<String>) () -> "some_response");

        Output<String> output = executor.execute(ac).get();

        assertThat(output.getThrowable()).hasMessage("timeout");
    }


}