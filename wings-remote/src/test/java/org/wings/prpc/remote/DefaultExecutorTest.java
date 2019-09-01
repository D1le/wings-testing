package org.wings.prpc.remote;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.objenesis.Objenesis;
import org.wings.prpc.remote.dependency.*;

import java.util.Date;

import static org.mockito.Mockito.mock;

class DefaultExecutorTest {

    public static class DateChecker implements Action<String> {
        @Override
        public String get() {
            return new Date().toString();
        }
    }

    public static class MockChecker implements Action<String> {
        @Override
        public String get() {
            return mock(Date.class).toString();
        }
    }


    @Test
    void name3() throws Exception {
        Executor executor = new DefaultExecutor.Builder()
                .withConfiguration(new PropertyFileConfiguration("wings-remote.properties"))
                .build();

        ActionContainer<String> ac = new ActionContainer<>();
        ac.setAction(new MockChecker());
        ac.dependsOn(new ClassDependency(MockChecker.class, true, false));
        ac.dependsOn(new PackageDependency(Action.class));
        ac.dependsOn(new JarDependency(Mockito.class));
        ac.dependsOn(new JarDependency(ByteBuddy.class));
        ac.dependsOn(new JarDependency(ByteBuddyAgent.class));
        ac.dependsOn(new JarDependency(Objenesis.class));
        ac.addDependencyResolver(new ClassDependencyResolver());
        ac.addDependencyResolver(new PackageDependencyResolver());
        ac.addDependencyResolver(new JarDependencyResolver());

        Output<String> stringOutput = executor.execute(ac).get();
        System.out.println(stringOutput);
    }
}