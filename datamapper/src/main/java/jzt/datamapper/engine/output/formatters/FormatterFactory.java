
package jzt.datamapper.engine.output.formatters;

import jzt.datamapper.engine.utils.ModelType;

/**
 * This class is a factory class to get {@link Formatter} needed by the data mapper engine
 */
public class FormatterFactory {

    public static Formatter getFormatter(ModelType formatterType) {
        switch (formatterType) {
            case JAVA_MAP:
                return new MapOutputFormatter();
            default:
                throw new IllegalArgumentException("Formatter for type " + formatterType + " is not implemented.");
        }
    }
}
