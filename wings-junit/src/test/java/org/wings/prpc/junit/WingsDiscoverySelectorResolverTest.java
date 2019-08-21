package org.wings.prpc.junit;

import dile.NewTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.engine.config.DefaultJupiterConfiguration;
import org.junit.jupiter.engine.config.JupiterConfiguration;
import org.junit.jupiter.engine.descriptor.JupiterEngineDescriptor;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by sp00x on 8/27/2019.
 * Project: wings-testing
 */
class WingsDiscoverySelectorResolverTest {

    @Test
    void name1() {
        EngineDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(NewTest.class))
                .build();

        JupiterConfiguration configuration = new DefaultJupiterConfiguration(request.getConfigurationParameters());
        JupiterEngineDescriptor jupiterEngineDescriptor =
                new JupiterEngineDescriptor(UniqueId.forEngine(JupiterEngineDescriptor.ENGINE_ID), configuration);
        WingsEngineDescriptor wingsEngineDescriptor =
                new WingsEngineDescriptor(UniqueId.forEngine(WingsEngineDescriptor.ENGINE_ID), configuration , jupiterEngineDescriptor);

        new WingsDiscoverySelectorResolver().resolveSelectors(request, wingsEngineDescriptor);
    }
}