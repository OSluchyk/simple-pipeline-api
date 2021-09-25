package os.demo.pipeline;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import os.toolset.config.PipelineConfig;
import os.toolset.pipeline.Context;

public class DataflowExecutionContext extends Context<PCollection<Row>> {
    private final Pipeline pipeline;

    public DataflowExecutionContext(Pipeline pipeline, PipelineConfig pipelineConfig) {
        super(pipelineConfig);
        this.pipeline = pipeline;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }
}
