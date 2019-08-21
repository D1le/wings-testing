package org.wings.prpc.junit;

import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;
import org.wings.prpc.junit.remote.JunitPlatformActionResult;
import org.wings.prpc.junit.remote.WingsTestExecutionResult;
import org.wings.prpc.remote.Output;

import java.util.concurrent.Future;

public class RemoteMethodTestDescriptor extends AbstractTestDescriptor implements Node<WingsEngineExecutionContext> {

    private TestMethodTestDescriptor delegate;

    public RemoteMethodTestDescriptor(TestMethodTestDescriptor delegate, JupiterConfiguration configuration) {
        super(delegate.getUniqueId(), delegate.getDisplayName());
        this.delegate = createDelegate(delegate, configuration);
    }

    public TestMethodTestDescriptor getDelegate() {
        return delegate;
    }

    private static TestMethodTestDescriptor createDelegate(TestMethodTestDescriptor delegate, JupiterConfiguration configuration) {
        return new TestMethodTestDescriptor(
                adjustId(delegate.getUniqueId()),
                delegate.getTestClass(),
                delegate.getTestMethod(),
                configuration);
    }

    private static UniqueId adjustId(UniqueId from) {
        UniqueId id = UniqueId.forEngine(JupiterEngineDescriptor.ENGINE_ID);
        for (int i = 1; i < from.getSegments().size(); i++) {
            id = id.append(from.getSegments().get(i));
        }
        return id;
    }

    @Override
    public Type getType() {
        return delegate.getType();
    }

    @Override
    public WingsEngineExecutionContext execute(WingsEngineExecutionContext context, DynamicTestExecutor dynamicTestExecutor) throws Exception {
        Future<Output<JunitPlatformActionResult>> actionResult = context.getRemoteExecutionResult();
        Output<JunitPlatformActionResult> junitPlatformActionResultOutput = actionResult.get();
        WingsTestExecutionResult wingsTestExecutionResult = junitPlatformActionResultOutput.getObject().get(delegate.getUniqueId());

        if (wingsTestExecutionResult != null) {
            TestExecutionResult.Status status = wingsTestExecutionResult.unwrap().getStatus();
            if (wingsTestExecutionResult.getLogs() != null) {
                System.out.println(wingsTestExecutionResult.getLogs());
            }
            if (status == TestExecutionResult.Status.FAILED) {
                throw new RuntimeException(wingsTestExecutionResult.unwrap().getThrowable().get());
            }
        } else {
            throw new RuntimeException("No results from remote test method execution");
        }
        return context;
    }
}
