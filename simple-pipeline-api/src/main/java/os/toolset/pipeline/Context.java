package os.toolset.pipeline;

import os.toolset.config.PipelineConfig;
import os.toolset.pipeline.stage.Stage;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The pipeline context is used to share information between the different pipeline stages.
 */
public class Context<T> {
    private final PipelineConfig pipelineConfig;
    private final Map<String, T> snapshots;
    private final Deque<Stage> completedStages;


    public Context(PipelineConfig pipelineConfig) {
        this.pipelineConfig = pipelineConfig;
        this.snapshots = new HashMap<>();
        this.completedStages = new LinkedList<>();
    }

    public T getSnapshot(String name) {
        return snapshots.get(name);
    }

    public void addSnapshot(String name, T snapshot) {
        snapshots.put(name, snapshot);
    }

    public PipelineConfig pipelineConfig() {
        return pipelineConfig;
    }

    public void markCompleted(Stage stage){
        completedStages.add(stage);
    }

    public Deque<Stage> getCompletedStages() {
        return completedStages;
    }
}
