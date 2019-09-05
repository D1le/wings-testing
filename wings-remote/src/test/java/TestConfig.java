import org.stagemonitor.configuration.ConfigurationOption;
import org.stagemonitor.configuration.ConfigurationOptionProvider;

public class TestConfig extends ConfigurationOptionProvider {

    private ConfigurationOption<String> tt = ConfigurationOption.stringOption()
            .key("tt")
            .buildWithDefault("zz");

    public String get(){
        return tt.get();
    };

}
