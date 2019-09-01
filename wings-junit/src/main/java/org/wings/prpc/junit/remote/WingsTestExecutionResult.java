package org.wings.prpc.junit.remote;

import org.junit.platform.engine.TestExecutionResult;

import java.io.Serializable;

public class WingsTestExecutionResult implements Serializable {

    private TestExecutionResult.Status status;
    private Throwable throwable;
    private String logs;

    public WingsTestExecutionResult() {
    }

    public TestExecutionResult.Status getStatus() {
        return status;
    }

    public void setStatus(TestExecutionResult.Status status) {
        this.status = status;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getLogs() {
        return logs;
    }

    public void setLogs(String logs) {
        this.logs = logs;
    }

    public TestExecutionResult unwrap() {
        switch (status) {
            case SUCCESSFUL:
                return TestExecutionResult.successful();
            case ABORTED:
                return TestExecutionResult.aborted(throwable);
            case FAILED:
                return TestExecutionResult.failed(throwable);
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return "WingsTestExecutionResult{" +
                "status=" + status +
                ", throwable=" + throwable +
                '}';
    }
}
