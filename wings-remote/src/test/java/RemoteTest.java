import org.junit.jupiter.api.Test;
import org.wings.prpc.remote.*;
import org.wings.prpc.remote.dependency.ClassDependency;
import org.wings.prpc.remote.dependency.ClassDependencyResolver;

public class RemoteTest {

    @Test
    void name() throws Exception {
        Executor executor = new DefaultExecutorBuilder().build();

        ActionContainer<String> ac = new ActionContainer<>();
        ac.setAction((Action<String>) () -> "hey!");
//        ac.dependsOn(new ClassDependency(RemoteTest.class, false, false));
        ac.addDependencyResolver(new ClassDependencyResolver());

        Output<String> output = executor.execute(ac).get();
        System.out.println(output);
    }
}
