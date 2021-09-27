package os.toolset.pipeline.stage;

import os.toolset.config.StageConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public final class StageRegistry {
    private final Map<String, Stage> registry;

    public StageRegistry() {
        this.registry = new HashMap<>();
        ServiceLoader<Stage> loader = ServiceLoader.load(Stage.class);
        for (Stage stage : loader) {
            registry.put(stage.name(), stage);
        }
    }

    public StageRegistry(Map<String, Stage> registry) {
        this.registry = registry;
    }

    public Stage get(StageConfig stageConfig) {
        Stage stage = registry.get(stageConfig.name());
        if (stageConfig.isProfilingEnabled()) stage = new ProfilingStage(stage);
        return stage;
    }


}
