
package jzt.datamapper.engine.core.exceptions;

import org.json.simple.parser.ParseException;

/**
 * This exception is thrown when engine gets an error parsing json with simple json
 */
public class SimpleJSONParserException extends ParseException {

    private String message = null;

    public SimpleJSONParserException(String message) {
        super(ERROR_UNEXPECTED_EXCEPTION);
        this.message = message;
    }

    @Override public String toString() {
        return message;
    }

    @Override public String getMessage() {
        return message;
    }
}
