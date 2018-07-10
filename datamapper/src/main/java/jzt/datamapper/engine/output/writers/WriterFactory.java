
package jzt.datamapper.engine.output.writers;

import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.exceptions.WriterException;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.utils.InputOutputDataType;

/**
 * This class is a factory class to get {@link Writer} needed by the data mapper engine
 */
public class WriterFactory {

    public static Writer getWriter(InputOutputDataType outputType, Schema outputSchema)
            throws SchemaException, WriterException {
        switch (outputType) {
            case XML:
                return new XMLWriter(outputSchema);
            case JSON:
                return new JSONWriter(outputSchema);
            case CSV:
                return new CSVWriter(outputSchema);
            default:
                throw new IllegalArgumentException("Output Writer for type " + outputType + " is not implemented.");
        }
    }
}
