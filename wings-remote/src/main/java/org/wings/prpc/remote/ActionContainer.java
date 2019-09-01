package org.wings.prpc.remote;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionContainer<R extends Serializable> {

    private Action<R> action;
    private Set<Dependency> dependencies = new HashSet<>();
    private List<DependencyResolver> dependencyResolvers = new ArrayList<>();

    public Action<R> getAction() {
        return action;
    }

    public void setAction(Action<R> action) {
        this.action = action;
    }

    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public List<DependencyResolver> getDependencyResolvers() {
        return dependencyResolvers;
    }

    public void setDependencyResolvers(List<DependencyResolver> dependencyResolvers) {
        this.dependencyResolvers = dependencyResolvers;
    }

    public void dependsOn(Dependency dependency) {
        dependencies.add(dependency);
    }

    public void addDependencyResolver(DependencyResolver resolver) {
        dependencyResolvers.add(resolver);
    }
}
