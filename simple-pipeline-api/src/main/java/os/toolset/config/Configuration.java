package os.toolset.config;

import java.io.Serializable;
import java.util.List;

public interface Configuration extends Serializable {

    ConfigValue<Boolean> getBoolean(String name);

    ConfigValue<String> getString(String name);

    ConfigValue<Integer> getInt(String name);

    ConfigValue<Double> getDouble(String name);

    TypesafeConfiguration getConfig(String name);

    List<TypesafeConfiguration> configList(String name);

    boolean isEmpty();
}
