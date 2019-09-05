package org.wings.prpc.remote;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.SimpleSource;
import org.wings.prpc.remote.configuration.ExecutorConfiguration;

import java.util.concurrent.Future;

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

}