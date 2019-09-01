package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Dependency;

import java.util.Objects;

/**
 * Dependency on jar file by containing class
 */
public class JarDependency implements Dependency {

    private final Class<?> classObj;

    public JarDependency(Class<?> classObj) {
        this.classObj = Objects.requireNonNull(classObj,
                "Class object for ClassDependency should npt be null");
    }

    public Class<?> getClassObj() {
        return classObj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JarDependency that = (JarDependency) o;
        return classObj.equals(that.classObj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classObj);
    }

    @Override
    public String toString() {
        return "JarDependency{" +
                "classObj=" + classObj +
                '}';
    }
}
