package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Dependency;

import java.util.Objects;

/**
 * Dependency on resource
 */
public class ResourceDependency implements Dependency {

    private final Class<?> classObj;
    private final String resourceName;

    ResourceDependency(Class<?> classObj, String resourceName) {
        this.classObj = classObj;
        this.resourceName = resourceName;
    }

    public Class<?> getClassObj() {
        return classObj;
    }

    public String getResourceName() {
        return resourceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceDependency that = (ResourceDependency) o;
        return resourceName.equals(that.resourceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceName);
    }

    @Override
    public String toString() {
        return "ResourceDependency{" +
                "resourceName='" + resourceName + '\'' +
                '}';
    }
}
