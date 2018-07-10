
package jzt.datamapper.engine.output.formatters;

import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.exceptions.WriterException;
import jzt.datamapper.engine.core.models.Model;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.output.OutputMessageBuilder;

/**
 * This interface should be implemented to convert data mapper generic models to
 */
public interface Formatter {

    void format(Model model, OutputMessageBuilder outputMessageBuilder, Schema outputSchema)
            throws SchemaException, WriterException;
}
