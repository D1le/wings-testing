package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Dependency;

import java.util.Objects;

/**
 * Dependency on package of java classes by containing class
 */
public class PackageDependency implements Dependency {

    private final Class<?> classObj;

    public PackageDependency(Class<?> classObj) {
        this.classObj = Objects.requireNonNull(classObj,
                "Class object for PackageDependency should not be null");
    }

    public Class<?> getClassObj() {
        return classObj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageDependency that = (PackageDependency) o;
        return classObj.equals(that.classObj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classObj);
    }

    @Override
    public String toString() {
        return "PackageDependency{" +
                "classObj=" + classObj +
                '}';
    }
}
