package os.toolset.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesafeConfiguration implements Serializable, Configuration {
    private static final Logger logger = LogManager.getLogger(TypesafeConfiguration.class);

    private final Config config;

    public TypesafeConfiguration(Config config) {
        this.config = config;
    }

    @Override
    public ConfigValue<Boolean> getBoolean(String name) {
        return get(name, config::getBoolean);
    }

    @Override
    public ConfigValue<String> getString(String name) {
        return get(name, config::getString);
    }

    @Override
    public ConfigValue<Integer> getInt(String name) {
        return get(name, config::getInt);
    }

    @Override
    public ConfigValue<Double> getDouble(String name) {
        return get(name, config::getDouble);
    }

    @Override
    public TypesafeConfiguration getConfig(String name) {
        Config config = get(name, this.config::getConfig).optional().orElse(ConfigFactory.empty());
        return new TypesafeConfiguration(config);
    }

    @Override
    public List<TypesafeConfiguration> configList(String name) {
        return get(name, config::getConfigList).optional().orElse(Collections.emptyList())
                .stream().map(TypesafeConfiguration::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isEmpty() {
        return config.isEmpty();
    }


    private <T> ConfigValue<T> get(String path, Function<String, T> getter) {
        return config.hasPath(path) ? new ConfigValue<>(path, getter.apply(path)) : new ConfigValue<>(path, null);
    }


    public static TypesafeConfiguration load(Path path) throws IOException {
        logger.info("Loading configuration from {}", path);
        return load(Files.newBufferedReader(path, StandardCharsets.UTF_8));
    }

    public static TypesafeConfiguration load(Reader reader) {
        return new TypesafeConfiguration(ConfigFactory.parseReader(reader));
    }

}
