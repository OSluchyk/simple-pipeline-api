package os.toolset.config;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public final class PipelineConfig extends BaseConfiguration {
    public static final String CONF_STAGES = "steps";
    private List<StageConfig> stageSeq;
    private Map<String, StageConfig> configMap;

    public PipelineConfig(Configuration conf) {
        super(conf);
    }

    public List<StageConfig> stageConfigs() {
        if (stageSeq == null) {
            stageSeq = configList(CONF_STAGES).stream()
                    .map(StageConfig::new)
                    .collect(toList());

        }
        return stageSeq;
    }


    public StageConfig stageConfig(String stgName) {
        if (configMap == null) {
            configMap = stageConfigs().stream()
                    .collect(toMap(StageConfig::name, identity()));
        }
        return configMap.get(stgName);
    }
}
