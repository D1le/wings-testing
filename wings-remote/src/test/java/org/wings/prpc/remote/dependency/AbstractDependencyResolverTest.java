package org.wings.prpc.remote.dependency;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.apache.tika.Tika;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

class AbstractDependencyResolverTest {

    static ScanResult scanResult;
    static Tika tika;

    @BeforeAll
    static void beforeAll() {
        scanResult = new ClassGraph().enableInterClassDependencies().scan();
        tika = new Tika();
    }

    @AfterAll
    static void afterAll() {
        scanResult.close();
    }
}