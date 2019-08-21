package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.Dependency;
import org.wings.prpc.remote.DependencyResolution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Set;

public class FileDependencyResolver extends AbstractDependencyResolver {

    @Override
    public DependencyResolution resolve(Dependency dependency) {
        if (dependency instanceof FileDependency) {
            FileDependency fileDependency = (FileDependency) dependency;

            File file = fileDependency.getFile();
            if (file.exists() && file.isFile() && file.canRead()) {
                return DependencyResolution.attachments(toAttachments(file));
            }
        }
        return DependencyResolution.unresolved();
    }

    private Set<Attachment> toAttachments(File file) {
        try {
            String name = file.getName();
            String type = getTika().detect(name);
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Collections.singleton(new Attachment(name, type, bytes));
        } catch (IOException ex) {
            return Collections.emptySet();
        }
    }
}
