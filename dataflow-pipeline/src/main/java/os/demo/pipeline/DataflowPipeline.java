package os.demo.pipeline;

import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import os.toolset.config.ApplicationConfig;
import os.toolset.config.PipelineConfig;
import os.toolset.pipeline.Context;
import os.toolset.pipeline.ExecutionError;
import os.toolset.pipeline.Pipeline;

import java.io.IOException;
import java.nio.file.Paths;

import static org.apache.beam.sdk.Pipeline.create;

public class DataflowPipeline extends Pipeline {
    private final org.apache.beam.sdk.Pipeline pipeline;

    public DataflowPipeline(org.apache.beam.sdk.Pipeline pipeline, PipelineConfig pipelineConfig) {
        super(pipelineConfig);
        this.pipeline = pipeline;
    }

    @Override
    public void run() throws ExecutionError {
        super.run();
        this.pipeline.run().waitUntilFinish();
    }

    @Override
    protected Context createExecutionContext() {
        return new DataflowExecutionContext(this.pipeline, pipelineConfig);
    }

    public interface Options extends PipelineOptions, StreamingOptions {
        String getPipelineConfigFile();

        void setPipelineConfigFile(String configFile);

    }

    public static void main(String[] args) throws IOException, ExecutionError {

        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        ApplicationConfig appConfig = ApplicationConfig.get();

        Pipeline pipeline = new DataflowPipeline(create(options), appConfig.pipelineConfig(Paths.get(options.getPipelineConfigFile())));
        pipeline.run();;

    }
}
