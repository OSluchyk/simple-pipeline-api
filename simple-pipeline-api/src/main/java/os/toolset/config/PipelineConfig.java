package os.toolset.config;

import java.util.List;
import java.util.stream.Collectors;

public final class PipelineConfig extends BaseConfiguration{
    public static final String CONF_STAGES="steps";

    public PipelineConfig(Configuration conf) {
        super(conf);
    }

    public List<StageConfig> stageConfigs(){
        return configList(CONF_STAGES).stream()
                .map(StageConfig::new)
                .collect(Collectors.toList());
    }


}
