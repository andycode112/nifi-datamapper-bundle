
package jzt.processors;

import jzt.datamapper.engine.core.exceptions.JSException;
import jzt.datamapper.engine.core.exceptions.ReaderException;
import jzt.datamapper.engine.core.exceptions.SchemaException;
import jzt.datamapper.engine.core.exceptions.WriterException;
import jzt.datamapper.engine.core.mapper.MappingHandler;
import jzt.datamapper.engine.core.mapper.MappingResource;
import org.apache.commons.lang.StringUtils;
import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.Stateful;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.components.Validator;
import org.apache.nifi.components.state.Scope;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Tags({"DataMapper"})
@CapabilityDescription("DataMapper")
@InputRequirement(InputRequirement.Requirement.INPUT_REQUIRED)
@Stateful(scopes = {Scope.LOCAL}, description = "Gives the option to store values not only on the FlowFile but as stateful variables to be referenced in a recursive manner.")
public class DataMapper extends AbstractProcessor {

//    public static final PropertyDescriptor MAP_CONFIG_URL = new PropertyDescriptor
//            .Builder().name("MAP_CONFIG_URL")
//            .displayName("映射配置Url")
//            .description("映射配置Url")
//            .required(true)
//            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
//            .build();
//
//    public static final PropertyDescriptor MAP_CONFIG_KEY = new PropertyDescriptor
//            .Builder().name("MAP_CONFIG_KEY")
//            .displayName("映射配置Key")
//            .description("映射配置Key")
//            .required(true)
//            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
//            .build();

    public static final PropertyDescriptor MAP_CONFIG_JS = new PropertyDescriptor
            .Builder().name("MAP_CONFIG_JS")
            .displayName("映射配置JS")
            .description("映射配置JS")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    public static final PropertyDescriptor INPUT_SCHEMA = new PropertyDescriptor
            .Builder().name("INPUT_SCHEMA")
            .displayName("输入Schema")
            .description("输入Schema")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    public static final PropertyDescriptor OUTPUT_SCHEMA = new PropertyDescriptor
            .Builder().name("OUTPUT_SCHEMA")
            .displayName("输出Schema")
            .description("输出Schema")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    @Override
    protected PropertyDescriptor getSupportedDynamicPropertyDescriptor(final String propertyDescriptorName) {
        return new PropertyDescriptor.Builder()
                .name(propertyDescriptorName)
                .expressionLanguageSupported(ExpressionLanguageScope.VARIABLE_REGISTRY)
                .addValidator(Validator.VALID)
                .required(false)
                .dynamic(true)
                .build();
    }

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .description("All successful FlowFiles are routed to this relationship").name("success").build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("Files that fail to send will transferred to failure")
            .build();
    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;
    private MappingResource mappingResource = null;
    private final AtomicBoolean clearState = new AtomicBoolean(false);


    @Override
    protected void init(final ProcessorInitializationContext context) {
        final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
//        descriptors.add(MAP_CONFIG_URL);
//        descriptors.add(MAP_CONFIG_KEY);JS
        descriptors.add(MAP_CONFIG_JS);
        descriptors.add(INPUT_SCHEMA);
        descriptors.add(OUTPUT_SCHEMA);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public void onPropertyModified(final PropertyDescriptor descriptor, final String oldValue, final String newValue) {
        clearState.set(true);
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) throws IOException {
        if (clearState.getAndSet(false)) {
            context.getStateManager().clear(Scope.LOCAL);
            mappingResource = null;
        }

    }
    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }


    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        final ComponentLog logger = getLogger();
        FlowFile flowFile = session.get();
        if (flowFile == null) {
            return;
        }
//        String url = context.getProperty(MAP_CONFIG_URL).evaluateAttributeExpressions(flowFile).getValue();
//        String configKey = context.getProperty(MAP_CONFIG_KEY).evaluateAttributeExpressions(flowFile).getValue();

        InputStream input = session.read(flowFile);

        if (logger.isDebugEnabled()) {
            logger.debug("Start : DataMapper mediator");
            if (logger.isTraceEnabled()) {
                logger.trace("Message :" + input);
            }
        }
        if (mappingResource == null) {

            String mapConfig = context.getProperty(MAP_CONFIG_JS).evaluateAttributeExpressions(flowFile).getValue();
            String inputSchema = context.getProperty(INPUT_SCHEMA).evaluateAttributeExpressions(flowFile).getValue();
            String outputSchema = context.getProperty(OUTPUT_SCHEMA).evaluateAttributeExpressions(flowFile).getValue();
            if (!(StringUtils.isNotEmpty(mapConfig) && StringUtils.isNotEmpty(inputSchema) &&
                    StringUtils.isNotEmpty(outputSchema))) {
                handleException("DataMapper processor : Invalid configurations");
                session.transfer(flowFile,REL_FAILURE);
            } else {
                // mapping resources needed to get the final output
                try {
                    mappingResource = getMappingResource(context, mapConfig, inputSchema, outputSchema);
                } catch (IOException e) {
                    handleException("DataMapper processor mapping resource generation failed", e);
                    session.transfer(flowFile,REL_FAILURE);
                }
            }
        }
        // Does message conversion and gives the final result
        String outputResult = null;
        try {
            Map<String, Map<String, Object>> propertiesMap;

//            String dmExecutorPoolSize = SynapsePropertiesLoader
//                    .getPropertyValue(ORG_APACHE_SYNAPSE_DATAMAPPER_EXECUTOR_POOL_SIZE, null);
            String dmExecutorPoolSize = "5";

            MappingHandler mappingHandler = new MappingHandler(mappingResource, "JSON", "JSON",
                    dmExecutorPoolSize);

            propertiesMap = getPropertiesMap(mappingResource.getPropertiesList(), context);

            /* execute mapping on the input stream */
            outputResult = mappingHandler
                    .doMap(input,
                            propertiesMap);
            String finalOutputResult = outputResult;
            flowFile = session.write(flowFile, new OutputStreamCallback() {
                @Override
                public void process(final OutputStream out) throws IOException {
                    out.write(finalOutputResult.getBytes(StandardCharsets.UTF_8));
                }
            });


        } catch (ReaderException | InterruptedException  | SchemaException
                | IOException | JSException | WriterException e) {
            handleException("DataMapper mediator : mapping failed", e);
            session.transfer(flowFile,REL_FAILURE);
        }
        //setting output type in the axis2 message context


        if (logger.isDebugEnabled()) {
            logger.debug("End : DataMapper processor");
            if (logger.isTraceEnabled()) {
                logger.trace("Message : " + outputResult);
            }
        }
        session.transfer(flowFile, REL_SUCCESS);

    }


    /**
     * When Data mapper mediator has been invoked initially, this creates a new mapping resource
     * loader
     *
     * @param context   message context
     * @param mapConfig the location of the mapping configuration
     * @param inSchema  the location of the input schema
     * @param outSchema the location of the output schema
     * @return the MappingResourceLoader object
     * @throws IOException
     */
    private MappingResource getMappingResource(ProcessContext context, String mapConfig, String inSchema,
                                               String outSchema) throws IOException {


        if (mapConfig == null) {
            handleException("DataMapper processor : mapping configuration is null");
        }

        if (inSchema == null) {
            handleException("DataMapper processor : input schema is null");
        }

        if (outSchema == null) {
            handleException("DataMapper processor : output schema is null");
        }

        try {
            // Creates a new mappingResourceLoader
            return new MappingResource(inSchema, outSchema, mapConfig, "JSON");
        } catch (SchemaException | JSException e) {
            handleException(e.getMessage());
        }
        return null;
    }

    private void handleException(String msg) {
        final ComponentLog logger = getLogger();
        logger.error(msg);
    }

    private void handleException(String msg, Exception e) {
        final ComponentLog logger = getLogger();
        logger.error(msg, e);
    }

    /**
     * Retrieve property values and insert into a map
     *
     * @param propertiesNamesList Required properties
     * @param context              Message context
     * @return Map filed with property name and the value
     */
    private Map<String, Map<String, Object>> getPropertiesMap(List<String> propertiesNamesList, ProcessContext context) {
        Map<String, Map<String, Object>> propertiesMap = new HashMap<>();

        String[] contextAndName;
        Object value;
        for (String propertyName : propertiesNamesList) {
            contextAndName = propertyName.split("\\['|'\\]");
            switch (contextAndName[0].toUpperCase()) {
                case "DEFAULT":
                    value = context.getProperty(contextAndName[1]).evaluateAttributeExpressions().getValue();
                    break;
                default:
                    getLogger().warn(contextAndName[0] + " scope is not found. Setting it to an empty value.");
                    value = "";
            }
            if (value == null) {
                getLogger().warn(propertyName + "not found. Setting it to an empty value.");
                value = "";
            }
            insertToMap(propertiesMap, contextAndName, value);
        }

        return propertiesMap;
    }

    /**
     * Insert a given value to the properties map
     *
     * @param propertiesMap  Reference to the properties map
     * @param contextAndName Context and the name of the property
     * @param value          Current value of the property
     */
    private void insertToMap(Map<String, Map<String, Object>> propertiesMap, String[] contextAndName, Object value) {
        Map<String, Object> insideMap = propertiesMap.get(contextAndName[0]);
        if (insideMap == null) {
            insideMap = new HashMap();
            propertiesMap.put(contextAndName[0], insideMap);
        }
        insideMap.put(contextAndName[1], value);
    }

}
