package os.toolset.pipeline.stage;

import org.apache.logging.log4j.Logger;
import os.toolset.pipeline.Context;
import os.toolset.pipeline.ExecutionError;

public class ProfilingStage implements Stage{
    private final Logger logger = logger();

    private final Stage stage;

    public ProfilingStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public String name() {
        return "profiling";
    }

    @Override
    public void run(Context context) throws ExecutionError {
        logger.info("Running {} stage", stage.name());
        long start = System.currentTimeMillis();
        stage.run(context);
        long end = System.currentTimeMillis();
        logger.info("It took {} ms to run {} stage", (end-start), stage.name());
    }
}
