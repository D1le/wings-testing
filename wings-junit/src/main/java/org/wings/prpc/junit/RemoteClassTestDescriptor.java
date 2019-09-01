package org.wings.prpc.junit;

import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.ClassTestDescriptor;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.commons.util.AnnotationUtils;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.AbstractTestDescriptor;
import org.junit.platform.engine.support.hierarchical.Node;
import org.wings.prpc.junit.annotation.DependsOnClasses;
import org.wings.prpc.junit.annotation.DependsOnJars;
import org.wings.prpc.junit.annotation.DependsOnPackages;
import org.wings.prpc.junit.remote.JunitPlatformAction;
import org.wings.prpc.junit.remote.JunitPlatformActionResult;
import org.wings.prpc.junit.remote.SerializableMethodSelector;
import org.wings.prpc.junit.remote.WingsTestExecutionResult;
import org.wings.prpc.remote.ActionContainer;
import org.wings.prpc.remote.Output;
import org.wings.prpc.remote.dependency.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class RemoteClassTestDescriptor extends AbstractTestDescriptor implements Node<WingsEngineExecutionContext> {

    private ClassTestDescriptor delegate;

    public RemoteClassTestDescriptor(ClassTestDescriptor delegate, JupiterConfiguration configuration) {
        super(delegate.getUniqueId(), delegate.getDisplayName());
        this.delegate = createDelegate(delegate, configuration);
    }

    private static ClassTestDescriptor createDelegate(ClassTestDescriptor delegate, JupiterConfiguration configuration) {
        return new ClassTestDescriptor(
                adjustId(delegate.getUniqueId()),
                delegate.getTestClass(),
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
    public Optional<TestSource> getSource() {
        return delegate.getSource();
    }

    @Override
    public Set<TestTag> getTags() {
        return delegate.getTags();
    }

    @Override
    public WingsEngineExecutionContext execute(WingsEngineExecutionContext context, DynamicTestExecutor dynamicTestExecutor) throws Exception {
        context.getThrowableCollector();
        List<SerializableMethodSelector> selectors = getChildren().stream()
                .filter(child -> child instanceof RemoteMethodTestDescriptor)
                .map(child -> (RemoteMethodTestDescriptor) child)
                .map(child -> new SerializableMethodSelector(
                        child.getDelegate().getTestClass(),
                        child.getDelegate().getTestMethod()))
                .collect(Collectors.toList());

        ActionContainer<JunitPlatformActionResult> container = new ActionContainer<>();
        container.setAction(new JunitPlatformAction(selectors));
        container.dependsOn(new ClassDependency(delegate.getTestClass(), true, false));
        container.addDependencyResolver(new ClassDependencyResolver());
        container.addDependencyResolver(new JarDependencyResolver());
        container.addDependencyResolver(new FileDependencyResolver());
        container.addDependencyResolver(new ResourceDependencyResolver());
        container.addDependencyResolver(new PackageDependencyResolver());

        Optional<DependsOnClasses> dependsOnClasses =
                AnnotationUtils.findAnnotation(delegate.getTestClass(), DependsOnClasses.class);
        if (dependsOnClasses.isPresent()) {
            Class<?>[] classes = dependsOnClasses.get().value();
            for (Class<?> classDependency : classes) {
                container.dependsOn(new ClassDependency(classDependency, true, false));
            }
        }

        Optional<DependsOnJars> dependsOnJars =
                AnnotationUtils.findAnnotation(delegate.getTestClass(), DependsOnJars.class);
        if (dependsOnJars.isPresent()) {
            Class<?>[] classes = dependsOnJars.get().value();
            for (Class<?> jarDependency : classes) {
                container.dependsOn(new JarDependency(jarDependency));
            }
        }

        Optional<DependsOnPackages> dependsOnPackages =
                AnnotationUtils.findAnnotation(delegate.getTestClass(), DependsOnPackages.class);
        if (dependsOnPackages.isPresent()) {
            Class<?>[] classes = dependsOnPackages.get().value();
            for (Class<?> packageDependency : classes) {
                container.dependsOn(new PackageDependency(packageDependency));
            }
        }

        Future<Output<JunitPlatformActionResult>> result = context.getRemoteExecutor().execute(container);
        result.get();
        context.setRemoteExecutionResult(result);

        return context;
    }

    @Override
    public void after(WingsEngineExecutionContext context) throws Exception {
        JunitPlatformActionResult object = context.getRemoteExecutionResult().get().getObject();
        WingsTestExecutionResult wingsTestExecutionResult = object.get(getUniqueId());
        if (wingsTestExecutionResult != null) {
            if (wingsTestExecutionResult.getThrowable() != null) {
                ExceptionUtils.throwAsUncheckedException(wingsTestExecutionResult.getThrowable());
            }
        } else {
//            throw new RuntimeException("No results from remote test class execution");
        }
    }

}
