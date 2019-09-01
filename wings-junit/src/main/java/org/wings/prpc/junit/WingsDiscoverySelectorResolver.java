package org.wings.prpc.junit;

import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.ClassTestDescriptor;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor;
import org.junit.jupiter.engine.discovery.DiscoverySelectorResolver;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.TestDescriptor;
import org.wings.prpc.junit.annotation.Local;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class WingsDiscoverySelectorResolver {


    public void resolveSelectors(EngineDiscoveryRequest discoveryRequest, WingsEngineDescriptor engineDescriptor) {
        new DiscoverySelectorResolver().resolveSelectors(discoveryRequest, engineDescriptor.getDelegate());
        resolveSelectors(engineDescriptor.getDelegate(), engineDescriptor);
    }

    private void resolveSelectors(JupiterEngineDescriptor jupiterEngineDescriptor, WingsEngineDescriptor wingsEngineDescriptor) {
        jupiterEngineDescriptor.getChildren().forEach(child -> {
            jupiterEngineDescriptor.removeChild(child);
            wingsEngineDescriptor.addChild(child);
            child.setParent(wingsEngineDescriptor);
        });

        LinkedList<TestDescriptor> stack = new LinkedList<>();
        stack.add(wingsEngineDescriptor);

        while (stack.size() > 0) {
            TestDescriptor current = stack.pop();
            TestDescriptor parent = current.getParent().orElse(null);
            Set<? extends TestDescriptor> children = new HashSet<>(current.getChildren());
            if (current instanceof ClassTestDescriptor) {
                Class<?> testClass = ((ClassTestDescriptor) current).getTestClass();
                if (!AnnotationUtils.isAnnotated(testClass, Local.class)) {
                    wrap(current, createRemoteClassTestDescriptor(current, jupiterEngineDescriptor.getConfiguration()));
                }
            } else if (current instanceof TestMethodTestDescriptor && parent instanceof RemoteClassTestDescriptor) {
                wrap(current, createRemoteMethodTestDescriptor(current, jupiterEngineDescriptor.getConfiguration()));
            }
            stack.addAll(0, children);
        }
    }

    private void wrap(TestDescriptor descriptor, TestDescriptor wrapper) {
        TestDescriptor parent = descriptor.getParent().orElse(null);
        HashSet<? extends TestDescriptor> children = new HashSet<>(descriptor.getChildren());
        descriptor.removeFromHierarchy();
        if (parent != null) {
            parent.addChild(wrapper);
        }
        wrapper.setParent(parent);
        children.forEach(child -> {
            wrapper.addChild(child);
            child.setParent(wrapper);
        });
    }

    private RemoteClassTestDescriptor createRemoteClassTestDescriptor(TestDescriptor descriptor,
                                                                      JupiterConfiguration configuration) {
        return new RemoteClassTestDescriptor((ClassTestDescriptor) descriptor, configuration);
    }

    private RemoteMethodTestDescriptor createRemoteMethodTestDescriptor(TestDescriptor current,
                                                                        JupiterConfiguration configuration) {
        return new RemoteMethodTestDescriptor((TestMethodTestDescriptor)current, configuration);
    }
}
