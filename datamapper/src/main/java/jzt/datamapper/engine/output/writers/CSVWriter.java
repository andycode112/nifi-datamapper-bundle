
package jzt.datamapper.engine.output.writers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.exceptions.WriterException;
import jzt.datamapper.engine.core.schemas.Schema;

/**
 * This class implements {@link Writer} interface and CSV writer for data mapper engine
 */
public class CSVWriter implements Writer {

    private static final Log log = LogFactory.getLog(CSVWriter.class);
    private Schema outputSchema;
    private StringBuilder csvOutputMessage;
    private boolean isStartingObject;

    public CSVWriter(Schema outputSchema) throws SchemaException, WriterException {
        this.outputSchema = outputSchema;
        this.csvOutputMessage = new StringBuilder();
        this.isStartingObject = true;
    }

    @Override
    public void writeField(String name, Object value) throws WriterException {
        if (!isStartingObject) {
            csvOutputMessage.append(",");
        } else {
            isStartingObject = false;
        }
        csvOutputMessage.append(value);
    }

    @Override
    public void writeStartAnonymousObject() throws WriterException {
        csvOutputMessage.append(System.getProperty("line.separator"));
        isStartingObject = true;
    }

    @Override
    public String terminateMessageBuilding() throws WriterException {
        return csvOutputMessage.toString();
    }

    @Override
    public void writePrimitive(Object value) throws WriterException {
    }

    @Override
    public void writeStartObject(String name) throws WriterException {
    }

    @Override
    public void writeEndObject(String objectName) throws WriterException {
    }

    @Override
    public void writeStartArray() {
    }

    @Override
    public void writeEndArray() throws WriterException {
    }
}
