package org.wings.prpc.junit.remote;

import org.junit.platform.commons.util.ReflectionUtils;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;

import java.io.Serializable;
import java.lang.reflect.Method;

public class SerializableMethodSelector implements Serializable {

    private String fullyQualifiedMethodName;

    public SerializableMethodSelector(Class<?> testClass, Method testMethod) {
        this.fullyQualifiedMethodName = ReflectionUtils.getFullyQualifiedMethodName(testClass, testMethod);
    }

    public MethodSelector unwrap() {
        return DiscoverySelectors.selectMethod(fullyQualifiedMethodName);
    }

    @Override
    public String toString() {
        return "SerializableMethodSelector{" +
                "fullyQualifiedMethodName='" + fullyQualifiedMethodName + '\'' +
                '}';
    }
}
