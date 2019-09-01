package org.wings.prpc.junit;

import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;

public class WingsEngineDescriptor extends EngineDescriptor implements Node<WingsEngineExecutionContext> {

    public static final String ENGINE_ID = "wings-junit-jupiter";

    private final JupiterConfiguration configuration;

    private JupiterEngineDescriptor delegate;

    public WingsEngineDescriptor(UniqueId uniqueId, JupiterConfiguration configuration,
                                 JupiterEngineDescriptor jupiterEngineDescriptor) {
        super(uniqueId, "Wings on JUnit Jupiter");
        this.configuration = configuration;
        this.delegate = jupiterEngineDescriptor;
    }

    public JupiterConfiguration getConfiguration() {
        return configuration;
    }

    public JupiterEngineDescriptor getDelegate() {
        return delegate;
    }

    @Override
    public ExecutionMode getExecutionMode() {
        return delegate.getExecutionMode();
    }

    @Override
    public WingsEngineExecutionContext prepare(WingsEngineExecutionContext context) {
        return new WingsEngineExecutionContext(delegate.prepare(context), context.getRemoteExecutor());
    }

    @Override
    public void cleanUp(WingsEngineExecutionContext context) throws Exception {
        context.close();
    }
}
