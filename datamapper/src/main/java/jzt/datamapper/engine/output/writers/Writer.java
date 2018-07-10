
package jzt.datamapper.engine.output.writers;

import jzt.datamapper.engine.core.exceptions.WriterException;

/**
 * This interface should be implemented by data-mapper output writers
 */
public interface Writer {

    void writeStartObject(String name) throws WriterException;

    void writeField(String name, Object value) throws WriterException;

    void writeEndObject(String objectName) throws WriterException;

    String terminateMessageBuilding() throws WriterException;

    void writeStartArray() throws WriterException;

    void writeEndArray() throws WriterException;

    void writeStartAnonymousObject() throws WriterException;

    void writePrimitive(Object value) throws WriterException;

}
