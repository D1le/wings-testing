package org.wings.prpc.junit;

import org.junit.jupiter.engine.config.CachingJupiterConfiguration;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.execution.JupiterEngineExecutionContext;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;
import org.wings.prpc.remote.DefaultExecutorBuilder;
import org.wings.prpc.remote.Executor;

public class WingsTestEngine extends HierarchicalTestEngine<WingsEngineExecutionContext> {

    @Override
    protected WingsEngineExecutionContext createExecutionContext(ExecutionRequest request) {
        JupiterEngineExecutionContext jupiterEngineExecutionContext = new JupiterEngineExecutionContext(
                request.getEngineExecutionListener(),
                getJupiterConfiguration(request));
        return new WingsEngineExecutionContext(jupiterEngineExecutionContext, createExecutor());
    }

    @Override
    public String getId() {
        return WingsEngineDescriptor.ENGINE_ID;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest discoveryRequest, UniqueId uniqueId) {
        JupiterConfiguration configuration = new CachingJupiterConfiguration(
                new DefaultJupiterConfiguration(discoveryRequest.getConfigurationParameters()));
        JupiterEngineDescriptor jupiterEngineDescriptor = new JupiterEngineDescriptor(uniqueId, configuration);

        WingsEngineDescriptor engineDescriptor = new WingsEngineDescriptor(uniqueId, configuration, jupiterEngineDescriptor);

        new WingsDiscoverySelectorResolver().resolveSelectors(discoveryRequest, engineDescriptor);

        return engineDescriptor;
    }

    private JupiterConfiguration getJupiterConfiguration(ExecutionRequest request) {
        WingsEngineDescriptor engineDescriptor = (WingsEngineDescriptor) request.getRootTestDescriptor();
        return engineDescriptor.getConfiguration();
    }

    private Executor createExecutor() {
        return new DefaultExecutorBuilder().build();
    }

}
