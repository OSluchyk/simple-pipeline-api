package os.toolset.pipeline.stage;

import os.toolset.config.StageConfig;
import os.toolset.pipeline.Context;
import os.toolset.pipeline.ExecutionError;
import os.toolset.utils.Loggable;

import java.io.Serializable;


/**
 * Stages in a pipeline represent the logical steps needed to process data.
 * Each stage represents a single high-level processing concept such as reading files, loading data from external storage, computing a product from the data, writing data to the database, etc.
 * <p>
 * Stage is supposed to be stateless. So, no information about previous processing is stored.
 */
public interface Stage<T> extends Loggable, Serializable {

    /**
     * @return unique identifier of the stage,
     */
    String name();

    /**
     * Determines if the stage is active and execution context contains all data required for
     * the stage execution
     *
     * @return
     */
    default boolean isReady(Context<T> context) {
        return true;
    }

    /**
     * @param context pipeline execution context
     * @throws ExecutionError
     */
    void run(Context<T> context) throws ExecutionError;

    /**
     * This method will be called when the pipeline execution failed
     * and can be used to roll back some changes.
     * For example this method can be used to clean up outputs generated by the stage.
     *
     * @param context pipeline execution context
     */
    default void onFailure(Context<T> context) {
        logger().info("Stage {}. Nothing to rollback.", name());
    }

    /**
     * This method is called if the whole pipeline execution was successful
     * and can be used to update some metadata required by this specific stage on the next run.
     * @param context pipeline execution context
     */
    default void onSuccess(Context<T> context) {
        logger().info("Stage {} is terminated", name());
    }

    default StageConfig stageConfig(Context<T> context) {
        return context.pipelineConfig().stageConfig(name());
    }
}
