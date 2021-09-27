package os.toolset.config;

import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.typesafe.config.ConfigFactory.parseReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newBufferedReader;

public final class ApplicationConfig extends BaseConfiguration{
    private static final Logger logger = LogManager.getLogger(ApplicationConfig.class);

    public static final String EXTERNAL_CONFIG_FILE="pipeline.conf.file";

    public ApplicationConfig(Configuration conf) {
        super(conf);
    }

    public PipelineConfig pipelineConfig() throws IOException {
        return pipelineConfig(Paths.get(getString(EXTERNAL_CONFIG_FILE).required()));

    }

    public PipelineConfig pipelineConfig(Path path) throws IOException {
        logger.info("Loading configuration from {}", path);
        TypesafeConfiguration baseConf = new TypesafeConfiguration(parseReader(newBufferedReader(path, UTF_8)));
        return new PipelineConfig(baseConf);

    }

    public static ApplicationConfig get() {
        return new ApplicationConfig(new TypesafeConfiguration(ConfigFactory.load()));
    }

}
