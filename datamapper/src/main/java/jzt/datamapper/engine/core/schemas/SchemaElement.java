
package jzt.datamapper.engine.core.schemas;

/**
 * This class hold properties of a element
 */
public class SchemaElement {

    private String elementName;
    private String namespace;

    public SchemaElement(String elementName, String namespace) {
        this.elementName = elementName;
        this.namespace = namespace;
    }

    public SchemaElement(String elementName) {
        this.elementName = elementName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
