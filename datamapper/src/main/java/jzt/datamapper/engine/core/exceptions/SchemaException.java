
package jzt.datamapper.engine.core.exceptions;

/**
 * This exception is thrown when engine gets an error processing schema
 */
public class SchemaException extends Exception {

    private String message = null;

    public SchemaException(String message) {
        super(message);
        this.message = message;
    }

    @Override public String toString() {
        return message;
    }

    @Override public String getMessage() {
        return message;
    }
}
