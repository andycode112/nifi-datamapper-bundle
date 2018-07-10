
package jzt.datamapper.engine.core.exceptions;

/**
 * This exception is thrown when engine gets a invalid payload with respect to schema
 */
public class InvalidPayloadException extends Exception {

    private String message = null;

    public InvalidPayloadException(String message) {
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
