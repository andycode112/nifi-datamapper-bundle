
package jzt.datamapper.engine.output;

import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.exceptions.WriterException;
import jzt.datamapper.engine.core.models.Model;
import jzt.datamapper.engine.core.notifiers.OutputVariableNotifier;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.input.readers.events.ReaderEvent;
import jzt.datamapper.engine.output.formatters.Formatter;
import jzt.datamapper.engine.output.formatters.FormatterFactory;
import jzt.datamapper.engine.output.writers.Writer;
import jzt.datamapper.engine.output.writers.WriterFactory;
import jzt.datamapper.engine.utils.InputOutputDataType;
import jzt.datamapper.engine.utils.ModelType;

public class OutputMessageBuilder {

    private Formatter formatter;
    private Writer outputWriter;
    private Schema outputSchema;
    private OutputVariableNotifier outputVariableNotifier;

    public OutputMessageBuilder(InputOutputDataType dataType, ModelType modelType, Schema outputSchema)
            throws SchemaException, WriterException {
        this.outputSchema = outputSchema;
        this.formatter = FormatterFactory.getFormatter(modelType);
        this.outputWriter = WriterFactory.getWriter(dataType, outputSchema);
    }

    public void buildOutputMessage(Model outputModel, OutputVariableNotifier mappingHandler)
            throws SchemaException, WriterException {
        this.outputVariableNotifier = mappingHandler;
        formatter.format(outputModel, this, outputSchema);
    }

    public void notifyEvent(ReaderEvent readerEvent) throws SchemaException, WriterException {
        switch (readerEvent.getEventType()) {
            case OBJECT_START:
                outputWriter.writeStartObject(readerEvent.getName());
                break;
            case FIELD:
                outputWriter.writeField(readerEvent.getName(), readerEvent.getValue());
                break;
            case OBJECT_END:
                outputWriter.writeEndObject(readerEvent.getName());
                break;
            case TERMINATE:
                outputVariableNotifier.notifyOutputVariable(outputWriter.terminateMessageBuilding());
                break;
            case ARRAY_START:
                outputWriter.writeStartArray();
                break;
            case ARRAY_END:
                outputWriter.writeEndArray();
                break;
            case ANONYMOUS_OBJECT_START:
                outputWriter.writeStartAnonymousObject();
                break;
        case PRIMITIVE:
            outputWriter.writePrimitive(readerEvent.getValue());
            break;
            default:
                throw new IllegalArgumentException("Unsupported reader event found : " + readerEvent.getEventType());
        }
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Schema outputSchema) {
        this.outputSchema = outputSchema;
    }

}
