package org.wings.prpc.remote.dependency;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.tika.Tika;
import org.wings.prpc.remote.*;
import org.wings.prpc.remote.dependency.AbstractDependencyResolver;

import java.util.*;

public class AttachmentResolver {

    public Set<Attachment> resolve(ActionContainer<?> action) {
        if(action.getDependencies().isEmpty()) {
            return Collections.emptySet();
        }

        Deque<Dependency> queue = new LinkedList<>(action.getDependencies());
        Set<Dependency> processed = new HashSet<>();
        Set<Attachment> attachments = new HashSet<>();

        ScanResult scanResult = new ClassGraph().enableInterClassDependencies().scan();
        Tika tika = new Tika();

        action.getDependencyResolvers().stream()
                .filter(resolver -> resolver instanceof AbstractDependencyResolver)
                .map(resolver -> (AbstractDependencyResolver) resolver)
                .forEach(resolver -> {
                    resolver.setScanResult(scanResult);
                    resolver.setTika(tika);
                });

        while (queue.size() > 0) {
//            boolean isUnresolved = true;
            Dependency dependency = queue.pop();
            for (DependencyResolver resolver : action.getDependencyResolvers()) {
                DependencyResolution resolution = resolver.resolve(dependency);
                Set<? extends Dependency> resolved = resolution.getDependencies();
                resolved.stream()
                        .filter(o -> !processed.contains(o))
                        .forEach(queue::add);
                processed.addAll(resolved);
                attachments.addAll(resolution.getAttachments());
            }
        }

        scanResult.close();
        return attachments;
    }
}
