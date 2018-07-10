
package jzt.datamapper.engine.input.builders;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.io.StringWriter;

import static jzt.datamapper.engine.utils.DataMapperEngineConstants.*;

/**
 * This class implements {@link Builder} interface and JSON builder for data mapper engine using
 * jackson
 */
public class JSONBuilder implements Builder {

    private JsonGenerator jsonGenerator;
    private StringWriter writer;

    public JSONBuilder() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        writer = new StringWriter();
        jsonGenerator = jsonFactory.createGenerator(writer);
    }

    @Override public void writeStartArray() throws IOException {
        jsonGenerator.writeStartArray();
    }

    @Override public void writeEndArray() throws IOException {
        jsonGenerator.writeEndArray();
    }

    @Override public void writeStartObject() throws IOException {
        jsonGenerator.writeStartObject();
    }

    @Override public void writeEndObject() throws IOException {
        jsonGenerator.writeEndObject();
    }

    @Override public void writeFieldName(String name) throws IOException {
        jsonGenerator.writeFieldName(name);
    }

    @Override public void writeString(String text) throws IOException {
        jsonGenerator.writeString(text);
    }

    @Override public void writeBinary(byte[] data, int offset, int len) throws IOException {
        jsonGenerator.writeBinary(data, offset, len);
    }

    @Override public void writeNumber(int number) throws IOException {
        jsonGenerator.writeNumber(number);
    }

    @Override public void writeNumber(double number) throws IOException {
        jsonGenerator.writeNumber(number);
    }

    @Override public void writeBoolean(boolean state) throws IOException {
        jsonGenerator.writeBoolean(state);
    }

    @Override public void writeStringField(String fieldName, String value) throws IOException {
        writeFieldName(fieldName);
        writeString(value);
    }

    @Override public void writeField(String fieldName, Object value, String fieldType) throws IOException {
        switch (fieldType) {
            case STRING_ELEMENT_TYPE:
                writeStringField(fieldName, (String) value);
                break;
            case BOOLEAN_ELEMENT_TYPE:
                writeBooleanField(fieldName, (Boolean) value);
                break;
            case NUMBER_ELEMENT_TYPE:
                writeNumberField(fieldName, (Double) value);
                break;
            case INTEGER_ELEMENT_TYPE:
                writeNumberField(fieldName, (Long) value);
                break;
            default:
                writeStringField(fieldName, (String) value);
        }
    }

    @Override public void writeBooleanField(String fieldName, boolean value) throws IOException {
        writeFieldName(fieldName);
        writeBoolean(value);
    }

    @Override public void writeNumberField(String fieldName, int value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    @Override public void writeNumberField(String fieldName, double value) throws IOException {
        writeFieldName(fieldName);
        writeNumber(value);
    }

    @Override public void writeBinaryField(String fieldName, byte[] data) throws IOException {
        writeFieldName(fieldName);
        writeBinary(data, 0, 0);
    }

    @Override public void writeArrayFieldStart(String fieldName) throws IOException {
        writeFieldName(fieldName);
        writeStartArray();
    }

    @Override public void writeObjectFieldStart(String fieldName) throws IOException {
        writeFieldName(fieldName);
        writeStartObject();
    }

    @Override public void close() throws IOException {
        jsonGenerator.close();
    }

    @Override public void writePrimitive(Object value, String fieldType) throws IOException {
        switch (fieldType) {
            case STRING_ELEMENT_TYPE:
                writeString((String) value);
                break;
            case BOOLEAN_ELEMENT_TYPE:
                writeBoolean((Boolean) value);
                break;
            case NUMBER_ELEMENT_TYPE:
                writeNumber((Double) value);
                break;
            case INTEGER_ELEMENT_TYPE:
                writeNumber((Long) value);
                break;
            default:
                writeString((String) value);
        }
    }

    public String getContent() throws IOException {
        String inputJSVariable = writer.toString();
        writer.close();
        return inputJSVariable;
    }

}
