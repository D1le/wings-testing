package org.wings.prpc.junit.remote;

import org.junit.platform.engine.UniqueId;

import java.io.Serializable;
import java.util.Map;

//TODO: use TestIdentifier instead of UniqueId
public class JunitPlatformActionResult implements Serializable {

    private Map<UniqueId, WingsTestExecutionResult> map;

    public JunitPlatformActionResult(Map<UniqueId, WingsTestExecutionResult> map) {
        this.map = map;
    }

    public WingsTestExecutionResult get(UniqueId id) {
        return map.get(id);
    }

    @Override
    public String toString() {
        return "NewJunitActionResult[" + map + "]";
    }
}
