package org.wings.prpc.remote;

import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

public class DependencyResolution {
    private static final DependencyResolution UNRESOLVED = new DependencyResolution(emptySet(), emptySet());

    private Set<? extends Dependency> dependencies;
    private Set<Attachment> attachments;

    DependencyResolution(Set<Dependency> dependencies, Set<Attachment> attachments) {
        this.dependencies = dependencies;
        this.attachments = attachments;
    }

    public Set<? extends Dependency> getDependencies() {
        return dependencies;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public static DependencyResolution unresolved() {
        return UNRESOLVED;
    }

    public static DependencyResolution attachment(Attachment attachment) {
        return new DependencyResolution(emptySet(), singleton(attachment));
    }

    public static DependencyResolution attachments(Set<Attachment> attachments) {
        return new DependencyResolution(emptySet(), attachments);
    }

    public static DependencyResolution dependency(Dependency dependency) {
        return new DependencyResolution(singleton(dependency), emptySet());
    }

    public static DependencyResolution dependencies(Set<Dependency> dependencies) {
        return new DependencyResolution(dependencies, emptySet());
    }

    public static DependencyResolution both(Set<Dependency> dependencies, Set<Attachment> attachments) {
        return new DependencyResolution(dependencies, attachments);
    }

    public boolean isResolved() {
        return !dependencies.isEmpty() || !attachments.isEmpty();
    }

    @Override
    public String toString() {
        return "Resolution{" +
                "dependencies=" + dependencies +
                ", attachments=" + attachments +
                '}';
    }
}
