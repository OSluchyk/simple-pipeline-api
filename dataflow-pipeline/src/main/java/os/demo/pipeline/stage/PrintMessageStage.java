package os.demo.pipeline.stage;

import com.google.auto.service.AutoService;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.Sample;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import os.demo.pipeline.DataflowExecutionContext;
import os.toolset.config.StageConfig;
import os.toolset.pipeline.ExecutionError;
import os.toolset.pipeline.stage.Stage;

@AutoService(Stage.class)
public class PrintMessageStage implements Stage<DataflowExecutionContext> {
    @Override
    public String name() {
        return "print-message";
    }

    @Override
    public void run(DataflowExecutionContext context) throws ExecutionError {
        StageConfig config = stageConfig(context);
        int limit = config.getInt("limit").orElse(10);
        String datasetId = config.getString("dataset").required();
        PCollection<Row> snapshot = context.getSnapshot(datasetId);
        snapshot.apply("limit", Sample.any(limit))
                .apply("Print", ParDo.of(new DoFn<Row, Row>() {
                    @DoFn.ProcessElement
                    public void process(@DoFn.Element Row element, DoFn.OutputReceiver<Row> receiver) {
                        System.out.println(element);
                        receiver.output(element);
                    }
                }))
        ;
    }
}
