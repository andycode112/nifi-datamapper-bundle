
package jzt.datamapper.engine.core.mapper;

import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.schemas.JacksonJSONSchema;
import jzt.datamapper.engine.core.schemas.Schema;
import jzt.datamapper.engine.utils.InputOutputDataType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jzt.datamapper.engine.utils.DataMapperEngineConstants.*;

public class MappingResource {

    public static final String NAMESPACE_DELIMETER = ":";
    public static final String NEW_LINE = "\n";
    private Schema inputSchema;
    private Schema outputSchema;
    private String inputRootelement;
    private String outputRootelement;
    private JSFunction function;
    private List<String> propertiesList;

    /**
     * @param inputSchema   respective output json schema as a a stream of bytes
     * @param outputSchema  respective output json schema as a a stream of bytes
     * @param mappingConfig mapping configuration file as a stream of bytes
     * @throws IOException when input errors, If there any parser exception occur while passing
     *                     above schemas method
     *                     will this exception
     */
    public MappingResource(String inputSchema, String outputSchema, String mappingConfig,
            String outputType) throws SchemaException, JSException {
        this.inputSchema = getJSONSchema(inputSchema);
        this.outputSchema = getJSONSchema(outputSchema);
        this.inputRootelement = this.inputSchema.getName();
        this.outputRootelement = this.outputSchema.getName();
        this.propertiesList = new ArrayList<>();
        this.function = createFunction(mappingConfig, outputType);
    }

    private Schema getJSONSchema(String inputSchema) throws SchemaException {
        return new JacksonJSONSchema(inputSchema);
    }

    public Schema getInputSchema() {
        return inputSchema;
    }

    public Schema getOutputSchema() {
        return outputSchema;
    }

    public JSFunction getFunction() {
        return function;
    }

    /**
     * propertiesList contains a list of WSO2 ESB Properties used in the Data Mapper Mapping configuration.
     * These will be extracted by processing the mapping configuration file and will be included as Strings
     * in the format of : "SCOPE['PROP_NAME']"
     *
     * @return propertiesList
     */
    public List getPropertiesList() {
        return propertiesList;
    }

    /**
     * need to create java script function by passing the configuration file
     * Since this function going to execute every time when message hit the mapping backend
     * so this function save in the resource model
     *
     * @param mappingConfig mapping configuration
     * @return java script function
     */
    private JSFunction createFunction(String mappingConfig, String outputType) throws JSException {
        //need to identify the main method of the configuration because that method going to
        // execute in engine
        String[] inputRootElementArray = inputRootelement.split(NAMESPACE_DELIMETER);
        String inputRootElement = inputRootElementArray[inputRootElementArray.length - 1];
        inputRootElement = inputRootElement.replaceAll("[:=,]", "_").replace(HYPHEN, ENCODE_CHAR_HYPHEN);
        String[] outputRootElementArray = outputRootelement.split(NAMESPACE_DELIMETER);
        String outputRootElement = outputRootElementArray[outputRootElementArray.length - 1];

        String propertiesPattern = "(DM_PROPERTIES.)([a-zA-Z_$][a-zA-Z_$0-9]*)\\['([a-zA-Z_$][a-zA-Z-_.$0-9]*)'\\]";
        Pattern pattern = Pattern.compile(propertiesPattern);
        Matcher match;

        String fnName =
                FUNCTION_NAME_CONST_1 + inputRootElement + FUNCTION_NAME_CONST_2 + outputRootElement + BRACKET_OPEN
                        + BRACKET_CLOSE;
        if (InputOutputDataType.JSON.toString().equals(outputType)) {
            fnName = JS_STRINGIFY + BRACKET_OPEN + fnName + BRACKET_CLOSE;
        }

        match = pattern.matcher(mappingConfig);

        while (match.find()) {
            propertiesList.add(match.group(2) + "['" + match.group(3) + "']");
        }

        if (fnName != null) {
            return new JSFunction(fnName, mappingConfig);
        } else {
            throw new JSException("Could not find mapping JavaScript function.");
        }
    }

}
