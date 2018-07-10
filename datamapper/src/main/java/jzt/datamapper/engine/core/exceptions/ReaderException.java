
package jzt.datamapper.engine.core.exceptions;

/**
 * This exception is thrown when engine gets an error when reading input or when building input
 */
public class ReaderException extends Exception {

    private String message = null;

    public ReaderException(String message) {
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
