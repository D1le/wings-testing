package org.wings.prpc.remote.dependency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wings.prpc.remote.DependencyResolution;
import org.wings.prpc.remote.dependency.testing.EmptyClass;
import org.wings.prpc.remote.dependency.testing.inner.AnotherEmptyClass;

import static org.assertj.core.api.Assertions.assertThat;

class PackageDependencyResolverTest extends AbstractDependencyResolverTest {

    private PackageDependencyResolver resolver;

    @BeforeEach
    void beforeEach() {
        resolver = new PackageDependencyResolver();
        resolver.setScanResult(scanResult);
        resolver.setTika(tika);
    }

    @Test
    void should_resolvePackageByClass1() {
        PackageDependency dependency = new PackageDependency(EmptyClass.class);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getAttachments()).isEmpty();
        assertThat(resolution.getDependencies()).hasSize(7);
    }

    @Test
    void should_resolvePackageByClass2() {
        PackageDependency dependency = new PackageDependency(AnotherEmptyClass.class);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getAttachments()).isEmpty();
        assertThat(resolution.getDependencies()).hasSize(1);
    }
}