package org.wings.prpc.remote.dependency;

import io.github.classgraph.ScanResult;
import org.apache.tika.Tika;
import org.wings.prpc.remote.DependencyResolver;

public abstract class AbstractDependencyResolver implements DependencyResolver {

    private ScanResult scanResult;
    private Tika tika;

    ScanResult getScanResult() {
        return scanResult;
    }

    void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    Tika getTika() {
        return tika;
    }

    void setTika(Tika tika) {
        this.tika = tika;
    }
}
