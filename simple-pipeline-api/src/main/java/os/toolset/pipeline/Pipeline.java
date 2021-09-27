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

    protected final PipelineConfig pipelineConfig;
    protected final StageRegistry stageRegistry;

    public Pipeline(PipelineConfig pipelineConfig) {
        this.pipelineConfig = pipelineConfig;
        this.stageRegistry = new StageRegistry();
    }

    public void run() throws ExecutionError {
        Context context = createExecutionContext();
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
        } catch (Exception error) {
            logger.error(format("Pipeline failed: {0}", error.getMessage()), error);
            Iterator<Stage> it = completed.descendingIterator();
            while (it.hasNext()) {
                it.next().revertChanges(context);
            }
            throw error;
        }

    }

    protected Context createExecutionContext() {
        return new Context(pipelineConfig);
    }

    public static void main(String[] args) throws IOException, ExecutionError {
        ApplicationConfig appConfig = ApplicationConfig.get();

        Pipeline pipeline = new Pipeline(appConfig.pipelineConfig());
        pipeline.run();

    }
}
