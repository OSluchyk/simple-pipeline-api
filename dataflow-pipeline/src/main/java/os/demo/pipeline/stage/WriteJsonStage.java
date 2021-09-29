package os.demo.pipeline.stage;

import com.google.auto.service.AutoService;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.ToJson;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import os.toolset.config.StageConfig;
import os.toolset.pipeline.Context;
import os.toolset.pipeline.ExecutionError;
import os.toolset.pipeline.stage.Stage;

@AutoService(Stage.class)
public class WriteJsonStage extends DataflowStage {
    @Override
    public String name() {
        return "save-as-json";
    }

    @Override
    public void run(Context<PCollection<Row>> context) throws ExecutionError {
        StageConfig stageConfig = stageConfig(context);

        PCollection<Row> input = context.getSnapshot(stageConfig.getString("input").required());
        input.apply("Convert to JSON", ToJson.of())
                .apply("Save JSON", TextIO.write().to(stageConfig.getString("output").required()));

    }
}
