package os.toolset.config;

import java.util.List;

public class BaseConfiguration implements Configuration {
    private final Configuration conf;

    public BaseConfiguration(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public ConfigValue<Boolean> getBoolean(String name) {
        return conf.getBoolean(name);
    }

    @Override
    public ConfigValue<String> getString(String name) {
        return conf.getString(name);
    }

    @Override
    public ConfigValue<Integer> getInt(String name) {
        return conf.getInt(name);
    }

    @Override
    public ConfigValue<Double> getDouble(String name) {
        return conf.getDouble(name);
    }

    @Override
    public TypesafeConfiguration getConfig(String name) {
        return conf.getConfig(name);
    }

    @Override
    public List<TypesafeConfiguration> configList(String name) {
        return conf.configList(name);
    }

    @Override
    public boolean isEmpty() {
        return conf.isEmpty();
    }
}
