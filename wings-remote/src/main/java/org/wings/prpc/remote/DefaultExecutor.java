package org.wings.prpc.remote;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.wings.prpc.remote.dependency.AttachmentResolver;

import java.io.*;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DefaultExecutor implements Executor {

    private OkHttpClient client;
    private String endpoint;
    private ExecutorConfiguration configuration;

    public DefaultExecutor() {
        this(new OkHttpClient.Builder().build());
    }

    DefaultExecutor(OkHttpClient okHttpClient) {
        client = okHttpClient;
    }

    DefaultExecutor(Builder builder) {
        this.configuration = builder.configuration;
        this.client = builder.okHttpClientBuilder
                .callTimeout(configuration.getTimeout(), TimeUnit.SECONDS)
                .build();
        this.endpoint = new HttpUrl.Builder()
                .scheme(builder.configuration.getScheme())
                .host(builder.configuration.getHost())
                .port(Integer.parseInt(builder.configuration.getPort()))
                .addPathSegments(configuration.getRestPath())
                .addPathSegments("mon/v1/test")
                .build()
                .toString();
    }

    @Override
    public <R extends Serializable> Future<Output<R>> execute(ActionContainer<R> container) {
        RequestBody body = prepareBody(container);
        Request request = new Request.Builder()
                .url(endpoint)
                .post(body)
                .build();

        CompletableFuture<Output<R>> future = new CompletableFuture<>();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.complete(Output.failed(e));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Output<R> output;
                if (!response.isSuccessful()) {
                    output = Output.failed(new IOException("Unexpected code " + response));
                } else {
                    try {
                        Object responseObject = deserialize(decode(response.body().string()));
                        if (responseObject instanceof Throwable) {
                            output = Output.failed((Throwable) responseObject);
                        } else {
                            output = Output.successful((R) responseObject);
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        output = Output.failed(ex);
                    }
                }
                response.close();
                future.complete(output);
            }
        });
        return future;
    }

    private byte[] serialize(Object object) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            return baos.toByteArray();
        }
    }

    private Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return ois.readObject();
        }
    }

    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] decode(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

    private RequestBody prepareBody(ActionContainer<?> container) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            bodyBuilder.addFormDataPart("input", encode(serialize(container.getAction())));
            handleDependencies(container, bodyBuilder);
        } catch (IOException /*| ClassNotFoundException*/ ex) {
            ex.printStackTrace();
        }
        return bodyBuilder.build();
    }

    private void handleDependencies(ActionContainer<?> container, MultipartBody.Builder bodyBuilder) {
        Set<Attachment> attachments = new AttachmentResolver().resolve(container);
        for (Attachment attachment : attachments) {
            bodyBuilder.addFormDataPart(attachment.getName(), attachment.getName(),
                    RequestBody.create(attachment.getBytes(), MediaType.parse(attachment.getType())));
        }
    }

    public static class Builder {
        private OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//        private String endpoint;
        private ExecutorConfiguration configuration;

        public Builder client(OkHttpClient.Builder okHttpClientBuilder) {
            this.okHttpClientBuilder = okHttpClientBuilder;
            return this;
        }

//        public Builder endpoint(String endpoint) {
//            this.endpoint = endpoint;
//            return this;
//        }

        public Builder withConfiguration(ExecutorConfiguration configuration) {
            this.configuration = configuration;
            return this;
        }

        public DefaultExecutor build() {
            return new DefaultExecutor(this);
        }
    }
}
