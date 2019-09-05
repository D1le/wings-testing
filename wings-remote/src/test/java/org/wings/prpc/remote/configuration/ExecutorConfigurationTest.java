package org.wings.prpc.remote.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExecutorConfigurationTest {

    @Test
    void name() {
        ExecutorConfiguration executorConfiguration = new ExecutorConfiguration();
        System.out.println(executorConfiguration.getHost());
        System.out.println(executorConfiguration.getExecutorEndpoint());
    }
}