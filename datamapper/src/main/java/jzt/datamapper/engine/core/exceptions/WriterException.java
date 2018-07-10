
package jzt.datamapper.engine.core.exceptions;

/**
 * This exception is thrown when engine gets an error writing output or when formatting
 */
public class WriterException extends Exception {

    private String message = null;

    public WriterException(String message) {
        super(message);
        this.message = message;
    }

    public WriterException(String message,Exception e) {
        super(message,e);
        this.message = message;
    }

    @Override public String toString() {
        return message;
    }

    @Override public String getMessage() {
        return message;
    }
}
