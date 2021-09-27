package os.toolset.config;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

public class ConfigValue<T> implements Serializable {
    private final String name;
    private final T value;

    public ConfigValue(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public Optional<T> optional() {
        return Optional.ofNullable(value);
    }

    public T orElse(T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public T required() {
        return optional().orElseThrow(() -> new NoSuchElementException("Configuration '" + name + "' is required"));
    }

    public T orNull() {
        return orElse(null);
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }
}
