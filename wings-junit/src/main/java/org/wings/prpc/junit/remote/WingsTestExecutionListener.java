package org.wings.prpc.junit.remote;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

import java.util.Map;

public class WingsTestExecutionListener implements TestExecutionListener {

    private final Map<UniqueId, WingsTestExecutionResult> map;

    public WingsTestExecutionListener(Map<UniqueId, WingsTestExecutionResult> map) {
        this.map = map;
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        UniqueId key = UniqueId.parse(testIdentifier.getUniqueId());
        map.compute(key, (k, v) -> {
            if (v == null) {
                v = new WingsTestExecutionResult();
            }
            v.setStatus(testExecutionResult.getStatus());
            v.setThrowable(testExecutionResult.getThrowable().orElse(null));
            return v;
        });
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        UniqueId key = UniqueId.parse(testIdentifier.getUniqueId());
        map.compute(key, (k, v) -> {
            if (v == null) {
                v = new WingsTestExecutionResult();
            }
            v.setLogs(entry.getKeyValuePairs().get("logs"));
            return v;
        });
    }
}
