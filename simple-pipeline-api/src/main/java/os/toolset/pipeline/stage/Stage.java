package os.toolset.pipeline.stage;

import os.toolset.pipeline.Context;
import os.toolset.pipeline.ExecutionError;

/**
 * Stages in a pipeline represent the logical steps needed to process data.
 * Each stage represents a single high-level processing concept such as reading files, loading data from external storage, computing a product from the data, writing data to the database, etc.
 * <p>
 * Stage is supposed to be stateless. So, no information about previous processing is stored.
 */
public interface Stage {

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
    default boolean isReady(Context context) {
        return true;
    }

    /**
     * @param context pipeline execution context
     * @throws ExecutionError
     */
    void run(Context context) throws ExecutionError;

    /**
     * this method will be called when the pipeline execution failed
     *
     * @param context pipeline execution context
     */
    default void revertChanges(Context context) {
        //do nothing by default
    }

    /**
     *
     * @param context
     */
    default void terminate(Context context) {
    }
}
