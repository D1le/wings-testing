package org.wings.prpc.remote.dependency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wings.prpc.remote.Attachment;
import org.wings.prpc.remote.DependencyResolution;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileDependencyResolverTest extends AbstractDependencyResolverTest {

    private FileDependencyResolver resolver;

    @BeforeEach
    void beforeEach() {
        resolver = new FileDependencyResolver();
        resolver.setTika(tika);
    }

    @Test
    void should_resolveFile_when_itExistsIsFileCanRead() throws Exception {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);
        when(file.getName()).thenReturn("some-file.txt");
        when(file.toPath()).thenReturn(getDummyFile().toPath());

        FileDependency dependency = new FileDependency(file);

        DependencyResolution resolution = resolver.resolve(dependency);
        assertThat(resolution.isResolved()).isTrue();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).hasSize(1);

        Attachment attachment = resolution.getAttachments().iterator().next();
        assertThat(attachment.getName()).isEqualTo("some-file.txt");
        assertThat(attachment.getType()).isEqualTo("text/plain");
        assertThat(attachment.getBytes()).isNotEmpty();
    }

    @Test
    void should_notResolveFile_when_itNotExistsIsFileCanRead() throws Exception {
        File file = mock(File.class);
//        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);

        FileDependency dependency = new FileDependency(file);

        DependencyResolution resolution = resolver.resolve(dependency);
        assertThat(resolution.isResolved()).isFalse();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).isEmpty();
    }

    @Test
    void should_notResolveFile_when_itExistsIsNotFileCanRead() throws Exception {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
//        when(file.isFile()).thenReturn(true);
        when(file.canRead()).thenReturn(true);

        FileDependency dependency = new FileDependency(file);

        DependencyResolution resolution = resolver.resolve(dependency);
        assertThat(resolution.isResolved()).isFalse();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).isEmpty();
    }

    @Test
    void should_notResolveFile_when_itExistsIsFileCanNotRead() throws Exception {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isFile()).thenReturn(true);
//        when(file.canRead()).thenReturn(true);

        FileDependency dependency = new FileDependency(file);

        DependencyResolution resolution = resolver.resolve(dependency);
        assertThat(resolution.isResolved()).isFalse();
        assertThat(resolution.getDependencies()).isEmpty();
        assertThat(resolution.getAttachments()).isEmpty();
    }

    private File getDummyFile() throws Exception {
        return new File(getClass().getResource(getClass().getSimpleName() + ".class").toURI());
    }
}