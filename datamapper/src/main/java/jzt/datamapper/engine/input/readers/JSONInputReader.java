
package jzt.datamapper.engine.input.readers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.core.exceptions.ReaderException;
import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.input.InputBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class is capable of parsing XML through AXIOMS for the InputStream and build the respective JSON message
 */
public class JSONInputReader implements InputReader {

    private static final Log log = LogFactory.getLog(JSONInputReader.class);

    /**
     * Constructor
     *
     * @throws IOException
     */
    public JSONInputReader() throws IOException {
    }

    /**
     * Read, parse the XML and notify with the output JSON message
     *
     * @param input          XML message InputStream
     * @param inputSchema    Schema of the input message
     * @param messageBuilder Reference of the InputXMLMessageBuilder
     * @throws ReaderException Exceptions in the parsing stage
     */
    @Override
    public void read(InputStream input, Schema inputSchema, InputBuilder messageBuilder) throws ReaderException {
        String inputJSONMessage;
        try {
            inputJSONMessage = readFromInputStream(input);
            messageBuilder.notifyWithResult(inputJSONMessage);
        } catch (IOException | JSException | SchemaException e) {
            throw new ReaderException("Error while reading input stream. " + e.getMessage());
        }

    }

    /**
     * Method added to convert the input directly into a string and to return
     * This method is used only when the JSON input is present
     *
     * @param inputStream JSON message as a InputStream
     * @return JSON message as a String
     * @throws IOException
     */
    private String readFromInputStream(InputStream inputStream) throws IOException {
        InputStreamReader isr = new InputStreamReader((inputStream),"UTF-8");
        BufferedReader br = new BufferedReader(isr);

        StringBuilder out = new StringBuilder("");
        String line;
        try {
            while ((line = br.readLine()) != null) {
                out.append(line);
            }
        } finally {
            br.close();
            isr.close();
        }
        return out.toString();
    }

}
