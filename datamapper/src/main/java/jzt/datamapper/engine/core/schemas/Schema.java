
package jzt.datamapper.engine.core.schemas;

import jzt.datamapper.engine.core.exceptions.InvalidPayloadException;
import jzt.datamapper.engine.core.exceptions.SchemaException;

import java.util.List;
import java.util.Map;

/**
 * Interface to represent schema in data mapper engine.
 */
public interface Schema {

    /**
     * Method for get defined name of the schema
     *
     * @return Name of the schema as a String
     */
    String getName() throws SchemaException;

    /**
     * Method to get the element type specified in the schema by giving the element hierarchy
     *
     * @param elementStack
     * @return type of the element
     */
    String getElementTypeByName(List<SchemaElement> elementStack) throws InvalidPayloadException, SchemaException;

    String getElementTypeByName(String elementStack) throws InvalidPayloadException, SchemaException;

    /**
     * Method for check whether schema has a child element inside given element
     *
     * @return
     */
    boolean isChildElement(String elementName, String childElementName);

    boolean isChildElement(List<SchemaElement> elementStack, String childElementName)
            throws InvalidPayloadException, SchemaException;

    String getPrefixForNamespace(String url);

    Map<String, String> getNamespaceMap();

    Map<String, String> getPrefixMap();

    boolean isCurrentArrayIsPrimitive();

    Map getSchemaMap();
}
