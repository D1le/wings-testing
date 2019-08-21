package org.wings.prpc.remote.dependency;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.tika.Tika;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.DependencyResolution;
import org.wings.prpc.remote.dependency.testing.ClassWithInnerClasses;
import org.wings.prpc.remote.dependency.testing.ClassWithReferences;
import org.wings.prpc.remote.dependency.testing.EmptyClass;

import static org.assertj.core.api.Assertions.assertThat;

class ClassDependencyResolverTest extends AbstractDependencyResolverTest {

    private ClassDependencyResolver resolver;

    @BeforeEach
    void beforeEach() {
        resolver = new ClassDependencyResolver();
        resolver.setScanResult(scanResult);
        resolver.setTika(tika);
    }

    @Test
    void should_resolveSimpleClass_when_noInnerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = EmptyClass.class;
        ClassDependency dependency = new ClassDependency(classObj, false, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveSimpleClass_when_innerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = EmptyClass.class;
        ClassDependency dependency = new ClassDependency(classObj, true, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveSimpleClass_when_innerClassAndDependencyResolutionRequired() {
        Class<?> classObj = EmptyClass.class;
        ClassDependency dependency = new ClassDependency(classObj, true, true);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveMultipleClasses_when_noInnerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = ClassWithInnerClasses.class;
        ClassDependency dependency = new ClassDependency(classObj, false, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveMultipleClasses_when_innerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = ClassWithInnerClasses.class;
        ClassDependency dependency = new ClassDependency(classObj, true, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();

        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();

        assertThat(resolution.getDependencies()).hasSize(4);
    }

    @Test
    void should_resolveMultipleClasses_when_innerClassAndDependencyResolutionRequired() {
        Class<?> classObj = ClassWithInnerClasses.class;
        ClassDependency dependency = new ClassDependency(classObj, true, true);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();

        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();

        assertThat(resolution.getDependencies()).hasSize(4);
    }

    @Test
    void should_resolveReferencingClasses_when_noInnerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = ClassWithReferences.class;
        ClassDependency dependency = new ClassDependency(classObj, false, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();

        assertThat(resolution.getAttachments()).hasSize(1);
        assertThat(resolution.getDependencies()).isEmpty();
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveReferencingClasses_when_innerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = ClassWithReferences.class;
        ClassDependency dependency = new ClassDependency(classObj, true, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();

        assertThat(resolution.getAttachments()).hasSize(1);
        assertThat(resolution.getDependencies()).isEmpty();
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveReferencingClasses_when_innerClassAndDependencyResolutionRequired() {
        Class<?> classObj = ClassWithReferences.class;
        ClassDependency dependency = new ClassDependency(classObj, true, true);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();

        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();

        assertThat(resolution.getDependencies()).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void should_resolveLibraryClass_when_noInnerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = Mockito.class;
        ClassDependency dependency = new ClassDependency(classObj, false, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getAttachments()).hasSize(1);
        assertThat(resolution.getDependencies()).isEmpty();
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveLibraryClass_when_innerClassAndNoDependencyResolutionRequired() {
        Class<?> classObj = Mockito.class;
        ClassDependency dependency = new ClassDependency(classObj, true, false);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getAttachments()).hasSize(1);
        assertThat(resolution.getDependencies()).isEmpty();
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_resolveLibraryClass_when_innerClassAndDependencyResolutionRequired() {
        Class<?> classObj = Mockito.class;
        ClassDependency dependency = new ClassDependency(classObj, true, true);

        DependencyResolution resolution = resolver.resolve(dependency);

        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getAttachments()).hasSize(1);
        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo(classObj.getName() + ".class");
        assertThat(attachment.getType()).isEqualTo("application/java-vm");
        assertThat(attachment.getBytes()).isNotEmpty();

        assertThat(resolution.getDependencies()).isNotEmpty();
    }

}