package org.wings.prpc.remote.dependency;

import io.github.classgraph.PackageInfo;
import org.wings.prpc.remote.Dependency;
import org.wings.prpc.remote.DependencyResolution;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * This resolver resolves {@code PackageDependency} instances.
 *
 * @author Alexey Lapin
 */
public class PackageDependencyResolver extends AbstractDependencyResolver {

    @Override
    public DependencyResolution resolve(Dependency dependency) {
        if (dependency instanceof PackageDependency) {
            PackageDependency packageDependency = (PackageDependency) dependency;

            String packageName = packageDependency.getClassObj().getPackage().getName();
            PackageInfo packageInfo = getScanResult().getPackageInfo(packageName);
            Set<Dependency> dependencies = packageInfo.getClassInfo()
                    .stream()
                    .map(c -> new ClassDependency(c.loadClass(), false, false))
                    .collect(Collectors.toSet());
            return DependencyResolution.dependencies(dependencies);
        }
        return DependencyResolution.unresolved();
    }
}
