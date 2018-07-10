
package jzt.datamapper.engine.input.builders;


import jzt.datamapper.engine.utils.ModelType;

import java.io.IOException;

/**
 * This class is a factory class to get {@link Builder} needed by the data mapper engine
 */
public class BuilderFactory {

    public static Builder getBuilder(ModelType inputType) throws IOException {
        switch (inputType) {
            case JSON_STRING:
                return new JSONBuilder();
            default:
                throw new IllegalArgumentException("Model builder for type " + inputType + " is not implemented.");
        }
    }
}
