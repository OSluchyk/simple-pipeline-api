package os.toolset.pipeline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import os.toolset.config.ApplicationConfig;
import os.toolset.config.PipelineConfig;
import os.toolset.pipeline.stage.Stage;
import os.toolset.pipeline.stage.StageRegistry;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;

import static java.text.MessageFormat.format;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class Pipeline {
    private final Logger logger = LogManager.getLogger();

    protected final PipelineConfig pipelineConfig;
    protected final StageRegistry stageRegistry;

    public Pipeline(PipelineConfig pipelineConfig) {
        this.pipelineConfig = pipelineConfig;
        this.stageRegistry = new StageRegistry();
    }

    public void run() throws ExecutionError {
        Context context = getContext();
        try {
            runPipeline(context);
            onSuccess(context);
        } catch (Exception error) {
            logger.error(format("Pipeline failed: {0}", error.getMessage()), error);
            onFailure(context);
            throw error;
        }

    }

    protected Context getContext() {
        return new Context(pipelineConfig);
    }

    protected void runPipeline(Context context) throws ExecutionError {
        for (Stage stage : stages(context)) {
            if (stage.isReady(context)) {
                stage.run(context);
                context.markCompleted(stage);
            } else {
                throw new ExecutionError(format("Precondition failed: Stage ''{0}'' cannot be executed.", stage.name()));
            }
        }
    }


    public void onFailure(Context context) {
        Iterator<Stage> it = context.getCompletedStages().descendingIterator();
        while (it.hasNext()) {
            it.next().onFailure(context);
        }
    }

    public void onSuccess(Context context) {
        Deque<Stage> completedStages = context.getCompletedStages();
        completedStages.forEach(stage -> stage.onSuccess(context));
    }

    private Iterable<? extends Stage> stages(Context context) {
        return context.pipelineConfig().stageConfigs()
                .stream()
                .map(conf -> requireNonNull(stageRegistry.get(conf), "Unknown stage " + conf.name()))
                .collect(toList());
    }


    public static void main(String[] args) throws IOException, ExecutionError {
        ApplicationConfig appConfig = ApplicationConfig.get();

        Pipeline pipeline = new Pipeline(appConfig.pipelineConfig());
        pipeline.run();

    }
}
