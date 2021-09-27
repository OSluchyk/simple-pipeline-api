package os.demo.pipeline.stage;

import com.google.auto.service.AutoService;
import org.apache.beam.sdk.coders.RowCoder;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.Row;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.Logger;
import os.demo.pipeline.DataflowExecutionContext;
import os.toolset.config.Configuration;
import os.toolset.config.StageConfig;
import os.toolset.pipeline.ExecutionError;
import os.toolset.pipeline.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.Channels;
import java.util.List;

import static org.apache.beam.sdk.io.FileIO.*;
import static org.apache.beam.sdk.schemas.Schema.Field.nullable;

@AutoService(Stage.class)
public class ReadCsvStage implements Stage<DataflowExecutionContext> {
    private final Logger logger = logger();
    @Override
    public String name() {
        return "read-csv";
    }

    @Override
    public void run(DataflowExecutionContext context) throws ExecutionError {
        StageConfig stageConfig = stageConfig(context);
        String inputPath = stageConfig.getString("inputFileSpec").required();
        logger.info("Loading data from {}", inputPath);

        Schema schema = generateSchema(stageConfig.getString("headers").required().split(","));
        PCollection<Row> inputCsv = context.getPipeline()
                .apply(match().filepattern(inputPath))
                .apply(readMatches())
                .apply("ReadCSV", ParDo.of(new CsvParser(stageConfig, schema))).setCoder(RowCoder.of(schema));

        context.addSnapshot(stageConfig.getString("output").orElse(name()), inputCsv);

    }

    private Schema generateSchema(String[] names) {
        Schema.Builder builder = Schema.builder();
        for (String name : names) {
            builder=builder.addField(nullable(name, Schema.FieldType.STRING));
        }
        return builder.build();
    }

    static class CsvParser extends DoFn<ReadableFile, Row> {
        private final Configuration conf;
        private final  Schema schema;

        CsvParser(Configuration conf,  Schema schema) {
            this.conf = conf;
            this.schema =schema;
        }

        @DoFn.ProcessElement
        public void process(@DoFn.Element FileIO.ReadableFile element, DoFn.OutputReceiver<Row> receiver) throws IOException {
            InputStream is = Channels.newInputStream(element.open());
            Reader reader = new InputStreamReader(is);
            Boolean hasHeader = conf.getBoolean("hasHeaders").orElse(false);

            CSVFormat.Builder csvFormat = CSVFormat.DEFAULT
                    .builder()
                    .setDelimiter(conf.getString("delimiter").orElse(","))
                    .setSkipHeaderRecord(hasHeader)
                    .setHeader(conf.getString("headers").required().split(","));


            CSVParser csvParser = csvFormat.build().parse(reader);
            List<String> headerNames = csvParser.getHeaderNames();


            for (CSVRecord csv : csvParser) {
                Row.Builder rowBuilder = Row.withSchema(schema);
                Row.FieldValueBuilder valueBuilder = null;

                for (Schema.Field field : schema.getFields()) {
                    String fieldName = field.getName();
                    String value = csv.get(fieldName);

                    valueBuilder = valueBuilder== null ? rowBuilder.withFieldValue(fieldName, value)
                    : valueBuilder.withFieldValue(fieldName, value);
                }
                receiver.output(valueBuilder.build());

            }
        }


    }
}
