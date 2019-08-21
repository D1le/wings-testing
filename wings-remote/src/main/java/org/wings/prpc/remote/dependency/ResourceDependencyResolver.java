package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.Dependency;
import org.wings.prpc.remote.DependencyResolution;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This resolver resolves {@code ResourceDependency} instances.
 *
 * @author Alexey Lapin
 */
public class ResourceDependencyResolver extends AbstractDependencyResolver {

    @Override
    public DependencyResolution resolve(Dependency dependency) {
        if (dependency instanceof ResourceDependency) {
            ResourceDependency resourceDependency = (ResourceDependency) dependency;

            URL resourceURL = resourceDependency.getClassObj()
                    .getResource(resourceDependency.getResourceName());

            if (resourceURL != null) {
                try {
                    String resourcePath;
                    if (resourceDependency.getResourceName().startsWith("/")) {
                        resourcePath = resourceDependency.getResourceName().replaceFirst("/", "");
                    } else {
                        resourcePath = resourceDependency.getClassObj().getPackage().getName() +
                                "." + resourceDependency.getResourceName();
                    }
                    Path filePath = Paths.get(resourceURL.toURI());
                    Attachment attachment = new Attachment(resourcePath,
                            getTika().detect(filePath.toString()), Files.readAllBytes(filePath));
                    return DependencyResolution.attachment(attachment);
                } catch (URISyntaxException | IOException ex) {

                }
            }
        }
        return DependencyResolution.unresolved();
    }
}
