
package jzt.datamapper.engine.core.executors;


import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.mapper.MappingResource;
import jzt.datamapper.engine.core.models.Model;

/**
 * This interface should be implemented by script executors of Data Mapper Engine
 */
public interface Executor {

    /**
     * Method to execute the mapping config in the {@link MappingResource} on
     * input variable and returns the output model
     *
     * @param mappingResource mapping resource model
     * @param inputVariable   input variable
     * @return model output model
     * @throws JSException if mapping throws an exception
     */
    public Model execute(MappingResource mappingResource, String inputVariable, String properties)
            throws JSException, SchemaException;
}
