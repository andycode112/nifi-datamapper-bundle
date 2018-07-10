
package jzt.datamapper.engine.core.mapper;

/**
 * This class will hold the data mapper mapping configuration
 */
public class JSFunction {

    private String functionName;
    private String functionBody;

    public JSFunction(String name, String body) {
        this.setFunctionName(name);
        this.setFunctionBody(body);
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionBody() {
        return functionBody;
    }

    public void setFunctionBody(String functionBody) {
        this.functionBody = functionBody;
    }

}
