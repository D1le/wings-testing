package org.wings.prpc.remote.dependency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.DependencyResolution;
import org.wings.prpc.remote.dependency.testing.EmptyClass;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceDependencyResolverTest extends AbstractDependencyResolverTest {

    private ResourceDependencyResolver resolver;

    @BeforeEach
    void beforeEach() {
        resolver = new ResourceDependencyResolver();
        resolver.setTika(tika);
    }

    @Test
    void should_resolve_relativeDependency() {
        ResourceDependency dependency = new ResourceDependency(EmptyClass.class, "res1.txt");

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);

        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(EmptyClass.class.getPackage().getName() + ".res1.txt");
        assertThat(attachment.getType()).isEqualTo("text/plain");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolve_absoluteDependency() {
        ResourceDependency dependency = new ResourceDependency(EmptyClass.class, "/res0.txt");

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);

        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo("res0.txt");
        assertThat(attachment.getType()).isEqualTo("text/plain");
        assertThat(attachment.getBytes()).isNotEmpty();
    }
}