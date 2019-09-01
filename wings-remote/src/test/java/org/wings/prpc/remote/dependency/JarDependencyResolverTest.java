package org.wings.prpc.remote.dependency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.DependencyResolution;
import org.wings.prpc.remote.dependency.testing.EmptyClass;

import static org.assertj.core.api.Assertions.assertThat;

class JarDependencyResolverTest extends AbstractDependencyResolverTest {

    private JarDependencyResolver resolver;

    @BeforeEach
    void beforeEach() {
        resolver = new JarDependencyResolver();
        resolver.setScanResult(scanResult);
        resolver.setTika(tika);
    }

    @Test
    void should_resolve_when_classInJar() {
        JarDependency jarDependency = new JarDependency(Mockito.class);

        DependencyResolution resolution = resolver.resolve(jarDependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);

        Attachment attachment = resolution.getAttachments().iterator().next();
        System.out.println(attachment);
        assertThat(attachment.getName()).startsWith("mockito").endsWith(".jar");
        assertThat(attachment.getType()).isEqualTo("application/java-archive");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_notResolve_when_classNotInJar() {
        JarDependency jarDependency = new JarDependency(EmptyClass.class);

        DependencyResolution resolution = resolver.resolve(jarDependency);

        assertThat(resolution.isResolved()).isFalse();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).isEmpty();
    }
}