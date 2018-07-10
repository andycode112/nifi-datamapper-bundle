
package jzt.datamapper.engine.core.exceptions;

/**
 * This exception is thrown when script executors can not execute the mapping configuration script
 */
public class JSException extends Exception {

    private String message = null;

    public JSException(String message) {
        super(message);
        this.message = message;
    }

    public JSException(String message,Exception e) {
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
