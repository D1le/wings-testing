package org.wings.prpc.remote;

import java.io.Serializable;

public class Output<S extends Serializable> {

    private Status status;
    private S object;
    private Throwable throwable;

    private Output(Status status, S object, Throwable throwable) {
        this.status = status;
        this.object = object;
        this.throwable = throwable;
    }

    public Status getStatus() {
        return status;
    }

    public S getObject() {
        return object;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public enum Status {
        SUCCESSFUL,
        FAILED
    }

    public static <S extends Serializable> Output<S> successful(S object) {
        return new Output<S>(Status.SUCCESSFUL, object, null);
    }

    public static <S extends Serializable> Output<S> failed(Throwable throwable) {
        return new Output<>(Status.FAILED, null, throwable);
    }

    @Override
    public String toString() {
        return "Output{" +
                "status=" + status +
                ", object=" + object +
                ", throwable=" + throwable +
                '}';
    }
}
