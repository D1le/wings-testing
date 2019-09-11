package org.wings.prpc.remote;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.wings.prpc.remote.configuration.ExecutorConfiguration;
import org.wings.prpc.remote.dependency.AttachmentResolver;

import java.io.*;
import java.util.Base64;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class DefaultExecutor implements Executor {

    private OkHttpClient client;
    private ExecutorConfiguration configuration;

    DefaultExecutor(OkHttpClient client, ExecutorConfiguration configuration) {
        this.client = client;
        this.configuration = configuration;
    }

    @Override
    public <R extends Serializable> Future<Output<R>> execute(ActionContainer<R> container) {
        CompletableFuture<Output<R>> future = new CompletableFuture<>();
        RequestBody body = createBody(container);
        Request request = createRequest(body);
        enqueueRequest(request, future);
        return future;
    }

    private Request createRequest(RequestBody body) {
        return new Request.Builder()
                .url(configuration.getExecutorEndpoint())
                .post(body)
                .build();
    }

    private <R extends Serializable> void enqueueRequest(Request request, CompletableFuture<Output<R>> future) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.complete(Output.failed(e));
            }

            @SuppressWarnings("unchecked")
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

    private RequestBody createBody(ActionContainer<?> container) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        try {
            bodyBuilder.addFormDataPart("input", encode(serialize(container.getAction())));
            handleDependencies(container, bodyBuilder);
        } catch (IOException ex) {
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
}
