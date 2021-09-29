package os.demo.pipeline.stage;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import os.demo.pipeline.DataflowExecutionContext;
import os.toolset.pipeline.Context;
import os.toolset.pipeline.stage.Stage;

public abstract class DataflowStage implements Stage<PCollection<Row>>  {

    protected Pipeline getPipeline(Context context) {
        return ((DataflowExecutionContext) context).getPipeline();
    }
}
