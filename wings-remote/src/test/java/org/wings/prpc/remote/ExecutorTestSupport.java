package org.wings.prpc.remote;

import okhttp3.MultipartBody;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.ConfigurationSource;
import org.stagemonitor.configuration.source.SimpleSource;
import org.wings.prpc.remote.configuration.ExecutorConfiguration;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.util.Base64;
import java.util.function.Supplier;

public class ExecutorTestSupport {

    public static final Dispatcher DEFAULT_DISPATCHER = new DefaultDispatcher();

    MockWebServer server;

    @BeforeEach
    void start() {
        server = new MockWebServer();
    }

    @AfterEach
    void stop() throws IOException {
        server.close();
    }

    ExecutorConfiguration getConfiguration(ConfigurationSource source) {
        return new ConfigurationRegistry.Builder()
                .addOptionProvider(new ExecutorConfiguration())
                .addConfigSource(source)
                .build()
                .getConfig(ExecutorConfiguration.class);
    }

    public static class DefaultDispatcher extends Dispatcher {
        @NotNull
        @Override
        public MockResponse dispatch(@NotNull RecordedRequest request) throws InterruptedException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try (InputStream ris = request.getBody().inputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                ByteArrayDataSource ds = new ByteArrayDataSource(ris, MultipartBody.FORM.toString());
                MimeMultipart multipart = new MimeMultipart(ds);
                BodyPart part = multipart.getBodyPart(0);

                InputStream pis = Base64.getDecoder().wrap(part.getInputStream());
                ObjectInputStream ois = new ObjectInputStream(pis);
                Object action = ois.readObject();
                if (action instanceof Supplier) {
                    Object result = ((Supplier) action).get();
                    oos.writeObject(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new MockResponse().setBody(Base64.getEncoder().encodeToString(baos.toByteArray()));
        }
    }
}
