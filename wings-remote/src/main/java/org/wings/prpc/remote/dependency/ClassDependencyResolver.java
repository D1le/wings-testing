package org.wings.prpc.remote.dependency;

import io.github.classgraph.ClassInfo;
import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.Dependency;
import org.wings.prpc.remote.DependencyResolution;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This resolver resolves {@code ClassDependency} instances.
 * If {@code ClassDependency} requires to resolve inner classes,
 * then additionally set of {@code ClassDependency} instances on inner classes will be returned.
 * If {@code ClassDependency} requires to resolve its class dependencies,
 * then additionally set of {@code ClassDependency} instances on referencing classes will be returned.
 *
 * @author Alexey Lapin
 */
public class ClassDependencyResolver extends AbstractDependencyResolver {

    @Override
    public DependencyResolution resolve(Dependency dependency) {
        if (dependency instanceof ClassDependency) {
            ClassDependency classDependency = (ClassDependency) dependency;
            ClassInfo classInfo = getScanResult().getClassInfo(classDependency.getClassObj().getName());
            Set<Dependency> dependencies = createDependenciesSet(classDependency);
            if (classDependency.shouldResolveInnerClasses()) {
                classInfo.getInnerClasses()
                        .stream()
                        .map(c -> c.loadClass(true))
                        .filter(Objects::nonNull)
                        .map(c -> new ClassDependency(c,
                                classDependency.shouldResolveInnerClasses(),
                                classDependency.shouldResolveDependencies()))
                        .forEach(dependencies::add);
            }
            if (classDependency.shouldResolveDependencies()) {
                classInfo.getClassDependencies()
                        .stream()
                        .map(c -> c.loadClass(true))
                        .filter(Objects::nonNull)
                        .map(c -> new ClassDependency(c,
                                classDependency.shouldResolveInnerClasses(),
                                classDependency.shouldResolveDependencies()))
                        .forEach(dependencies::add);
            }
            return DependencyResolution.both(dependencies, toAttachments(classInfo));
        }
        return DependencyResolution.unresolved();
    }

    private Set<Attachment> toAttachments(ClassInfo classInfo) {
        try {
            String name = classInfo.getName() + ".class";
            String type = getTika().detect(classInfo.getResource().getPath());
            byte[] bytes = classInfo.getResource().load();
            return Collections.singleton(new Attachment(name, type, bytes));
        } catch (IOException ex) {
            return Collections.emptySet();
        }
    }

    private Set<Dependency> createDependenciesSet(ClassDependency classDependency) {
        if (classDependency.shouldResolveInnerClasses() || classDependency.shouldResolveDependencies()) {
            return new HashSet<>();
        }
        return Collections.emptySet();
    }
}
