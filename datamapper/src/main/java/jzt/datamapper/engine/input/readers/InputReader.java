
package jzt.datamapper.engine.input.readers;

import jzt.datamapper.engine.core.exceptions.ReaderException;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.input.InputBuilder;

import java.io.InputStream;

/**
 * This interface should be implemented by data-mapper input readers.
 */
public interface InputReader {

    /**
     *
     * @param input
     * @param inputSchema
     * @param messageBuilder
     * @throws ReaderException
     */
    void read(InputStream input, Schema inputSchema, InputBuilder messageBuilder) throws ReaderException;
}
