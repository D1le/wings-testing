package org.wings.prpc.remote.dependency;

import io.github.classgraph.ClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.Dependency;
import org.wings.prpc.remote.DependencyResolution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Set;

/**
 * This resolver resolves {@code JarDependency} instances.
 *
 * @author Alexey Lapin
 */
public class JarDependencyResolver extends AbstractDependencyResolver {

//    private static final Logger logger = LoggerFactory.getLogger(JarDependencyResolver.class);

    @Override
    public DependencyResolution resolve(Dependency dependency) {
        if (dependency instanceof JarDependency) {
            JarDependency jarDependency = (JarDependency) dependency;

            ClassInfo classInfo = getScanResult().getClassInfo(jarDependency.getClassObj().getName());
            File classpathElementFile = classInfo.getResource().getClasspathElementFile();
            if (classpathElementFile.getName().endsWith("jar")) {
                return DependencyResolution.attachments(toAttachments(classpathElementFile));
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
//            logger.error("Failed to construct attachment", ex);
            return Collections.emptySet();
        }
    }
}
