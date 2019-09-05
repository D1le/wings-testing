import org.junit.jupiter.api.Test;
import org.stagemonitor.configuration.ConfigurationRegistry;
import org.stagemonitor.configuration.source.SimpleSource;

public class ConfigurationTest {


    @Test
    void name() {
//        new PropertyFileConfigurationSource()
        SimpleSource source = new SimpleSource();
        source.add("tt", "ff");

        ConfigurationRegistry registry = ConfigurationRegistry.builder()
                .addConfigSource(source)
                .addOptionProvider(new TestConfig())
                .build();

        String s = registry.getConfig(TestConfig.class).get();
        System.out.println(s);
    }
}
