
package jzt.datamapper.engine.input;

import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.core.exceptions.ReaderException;
import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.notifiers.InputVariableNotifier;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.input.readers.InputReader;
import jzt.datamapper.engine.input.readers.InputReaderFactory;
import jzt.datamapper.engine.utils.InputOutputDataType;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class manage the XML to JSON parsing process
 */
public class InputBuilder {

    private InputReader inputReader;
    private Schema inputSchema;
    private InputVariableNotifier inputVariableNotifier;

    /**
     * Constructor
     *
     * @param inputSchema Input message JSON schema
     * @throws IOException
     */
    public InputBuilder(InputOutputDataType inputType, Schema inputSchema) throws IOException {
        this.inputReader = InputReaderFactory.getReader(inputType);
        this.inputSchema = inputSchema;
    }

    /**
     * @param inputStream           XML input message
     * @param inputVariableNotifier Reference to the MappingHandler instance
     * @throws ReaderException
     */
    public void buildInputModel(InputStream inputStream, InputVariableNotifier inputVariableNotifier)
            throws ReaderException {
        this.inputVariableNotifier = inputVariableNotifier;
        inputReader.read(inputStream, inputSchema, this);
    }

    /**
     * This method will be called by the XMLInputReader instance to notify with the output
     *
     * @param builtMessage Built JSON message
     * @throws JSException
     * @throws ReaderException
     * @throws SchemaException
     */
    public void notifyWithResult(String builtMessage) throws JSException, ReaderException, SchemaException {
        inputVariableNotifier.notifyInputVariable(builtMessage);
    }

}
