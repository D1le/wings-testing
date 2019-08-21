package org.wings.prpc.remote.dependency;

import org.wings.prpc.remote.Dependency;

import java.io.File;
import java.util.Objects;

/**
 * Dependency on file
 */
public class FileDependency implements Dependency {

    private final File file;

    FileDependency(File file) {
        this.file = Objects.requireNonNull(file,
                "File for FileDependency should not be null");
    }

    public File getFile() {
        return file;
    }

    public static FileDependency ofFile(File file) {
        return new FileDependency(file);
    }

    public static FileDependency ofPath(String path) {
        return new FileDependency(new File(path));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDependency that = (FileDependency) o;
        return file.equals(that.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file);
    }

    @Override
    public String toString() {
        return "FileDependency{" +
                "file=" + file +
                '}';
    }
}
