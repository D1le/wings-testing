package org.wings.prpc.junit;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.execution.JupiterEngineExecutionContext;
import org.junit.jupiter.engine.execution.TestInstancesProvider;
import org.junit.jupiter.engine.extension.MutableExtensionRegistry;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.support.hierarchical.ThrowableCollector;
import org.wings.prpc.junit.remote.JunitPlatformActionResult;
import org.wings.prpc.remote.Executor;
import org.wings.prpc.remote.Output;

import java.util.concurrent.Future;

// TODO: refactor - not subclassing, only delegate
public class WingsEngineExecutionContext extends JupiterEngineExecutionContext {

    private final JupiterEngineExecutionContext delegate;

    private final Executor remoteExecutor;

    private Future<Output<JunitPlatformActionResult>> remoteExecutionResult;

    public WingsEngineExecutionContext(JupiterEngineExecutionContext context, Executor remoteExecutor) {
        super(null, null);
        this.delegate = context;
        this.remoteExecutor = remoteExecutor;
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    @Override
    public EngineExecutionListener getExecutionListener() {
        return delegate.getExecutionListener();
    }

    @Override
    public JupiterConfiguration getConfiguration() {
        return delegate.getConfiguration();
    }

    @Override
    public TestInstancesProvider getTestInstancesProvider() {
        return delegate.getTestInstancesProvider();
    }

    @Override
    public MutableExtensionRegistry getExtensionRegistry() {
        return delegate.getExtensionRegistry();
    }

    @Override
    public ExtensionContext getExtensionContext() {
        return delegate.getExtensionContext();
    }

    @Override
    public ThrowableCollector getThrowableCollector() {
        return delegate.getThrowableCollector();
    }

    @Override
    public void beforeAllCallbacksExecuted(boolean beforeAllCallbacksExecuted) {
        delegate.beforeAllCallbacksExecuted(beforeAllCallbacksExecuted);
    }

    @Override
    public boolean beforeAllCallbacksExecuted() {
        return delegate.beforeAllCallbacksExecuted();
    }

    @Override
    public void beforeAllMethodsExecuted(boolean beforeAllMethodsExecuted) {
        delegate.beforeAllMethodsExecuted(beforeAllMethodsExecuted);
    }

    @Override
    public boolean beforeAllMethodsExecuted() {
        return delegate.beforeAllMethodsExecuted();
    }

    @Override
    public JupiterEngineExecutionContext.Builder extend() {
        return delegate.extend();
    }

    public Executor getRemoteExecutor() {
        return remoteExecutor;
    }

    public void setRemoteExecutionResult(Future<Output<JunitPlatformActionResult>> result) {
        this.remoteExecutionResult = result;
    }

    public Future<Output<JunitPlatformActionResult>> getRemoteExecutionResult() {
        return remoteExecutionResult;
    }
}
