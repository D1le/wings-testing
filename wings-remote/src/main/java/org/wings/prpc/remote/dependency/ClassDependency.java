package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Dependency;

import java.util.Objects;

/**
 * Dependency on java class
 */
public class ClassDependency implements Dependency {

    private final Class<?> classObj;
    private final boolean shouldResolveInnerClasses;
    private final boolean shouldResolveDependencies;

    public ClassDependency(Class<?> classObj, boolean shouldResolveInnerClasses, boolean shouldResolveDependencies) {
        this.classObj = Objects.requireNonNull(classObj,
                "Class object for ClassDependency should npt be null");
        this.shouldResolveInnerClasses = shouldResolveInnerClasses;
        this.shouldResolveDependencies = shouldResolveDependencies;
    }

    public Class<?> getClassObj() {
        return classObj;
    }

    public boolean shouldResolveInnerClasses() {
        return shouldResolveInnerClasses;
    }

    public boolean shouldResolveDependencies() {
        return shouldResolveDependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDependency that = (ClassDependency) o;
        return classObj.equals(that.classObj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classObj);
    }

    @Override
    public String toString() {
        return "ClassDependency{" +
                "classObj=" + classObj +
                '}';
    }
}
