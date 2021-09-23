package os.toolset.pipeline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import os.toolset.config.ApplicationConfig;
import os.toolset.config.PipelineConfig;
import os.toolset.config.StageConfig;
import os.toolset.pipeline.stage.Stage;
import os.toolset.pipeline.stage.StageRegistry;

import java.io.IOException;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import static java.text.MessageFormat.format;

public class Pipeline {
    private final Logger logger = LogManager.getLogger();

    private final PipelineConfig pipelineConfig;
    private final StageRegistry stageRegistry;

    public Pipeline(PipelineConfig pipelineConfig) {
        this.pipelineConfig = pipelineConfig;
        this.stageRegistry = new StageRegistry();
    }

    public void run() throws ExecutionError {
        Context context = new Context(pipelineConfig);
        Deque<Stage> completed = new LinkedList<>();
        try {
            for (StageConfig stageConfig : context.pipelineConfig().stageConfigs()) {
                Stage stage = stageRegistry.get(stageConfig);
                if (stage.isReady(context)) {
                    stage.run(context);
                    completed.add(stage);
                }else{
                    throw new ExecutionError(format("Precondition failed: Stage ''{0}'' cannot be executed.", stage.name()));
                }
            }
            completed.forEach(stg -> stg.terminate(context));
        } catch (ExecutionError executionError) {
            logger.error(format("Pipeline failed: {0}", executionError.getMessage()), executionError);
            Iterator<Stage> it = completed.descendingIterator();
            while (it.hasNext()) {
                it.next().revertChanges(context);
            }
            throw executionError;
        }

    }

    public static void main(String[] args) throws IOException, ExecutionError {
        ApplicationConfig appConfig = ApplicationConfig.get();

        Pipeline pipeline = new Pipeline(appConfig.pipelineConfig());
        pipeline.run();

    }
}
