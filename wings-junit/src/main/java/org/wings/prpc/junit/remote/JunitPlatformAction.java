package org.wings.prpc.junit.remote;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.wings.prpc.remote.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JunitPlatformAction implements Action<JunitPlatformActionResult> {

    private List<SerializableMethodSelector> selectors;

    public JunitPlatformAction(List<SerializableMethodSelector> selectors) {
        this.selectors = selectors;
    }

    @Override
    public JunitPlatformActionResult get() {
        Launcher launcher = LauncherFactory.create();
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectors.stream()
                        .map(SerializableMethodSelector::unwrap)
                        .collect(Collectors.toList()))
                .build();

        Map<UniqueId, WingsTestExecutionResult> map = new HashMap<>();
        TestExecutionListener listener = new WingsTestExecutionListener(map);

        launcher.execute(request, listener);

        return new JunitPlatformActionResult(map);
    }

    @Override
    public String toString() {
        return "JunitPlatformAction{" +
                "selectors=" + selectors +
                '}';
    }
}
