
package jzt.datamapper.engine.core.executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static jzt.datamapper.engine.utils.DataMapperEngineConstants
        .DEFAULT_DATAMAPPER_ENGINE_POOL_SIZE;

/**
 * This class act as a factory to get the requested script executor
 */
public class ScriptExecutorFactory {

    private static ScriptExecutorPool executorPool = null;
    private static ScriptExecutorType scriptExecutorType = ScriptExecutorType.NASHORN;
    private static final Log log = LogFactory.getLog(ScriptExecutorFactory.class);

    /**
     * This private constructor added to hide the implicit public constructor
     */
    private ScriptExecutorFactory() {
    }

    /**
     * This method will return the script executor according to the given {@link
     * ScriptExecutorType}
     *
     * @return script executor
     */
    public static Executor getScriptExecutor(String executorPoolSize) throws InterruptedException {
        if (executorPool == null) {
            initializeExecutorPool(executorPoolSize);
        }
        return executorPool.take();
    }

    /**
     * Initialize a script executors pool. If Java8, use Nashorn as the script engine or if Java7
     * or 6 use Rhino
     * which is the default javascript engine provided in Java
     *
     * @param executorPoolSizeStr size of the executor pool
     */
    private synchronized static void initializeExecutorPool(String executorPoolSizeStr) {
        if (executorPool == null) {
            String javaVersion = System.getProperty("java.version");
            if (javaVersion.startsWith("1.7") || javaVersion.startsWith("1.6")) {
                scriptExecutorType = ScriptExecutorType.RHINO;
                log.debug("Script Engine set to Rhino");
            } else {
                log.debug("Script Engine set to Nashorn");
            }

            int executorPoolSize = DEFAULT_DATAMAPPER_ENGINE_POOL_SIZE;
            if (executorPoolSizeStr != null) {
                executorPoolSize = Integer.parseInt(executorPoolSizeStr);
                log.debug("Script executor pool size set to " + executorPoolSize);
            } else {
                log.debug("Using default script executor pool size " + executorPoolSize);
            }
            executorPool = new ScriptExecutorPool(scriptExecutorType, executorPoolSize);
        }
    }

    /**
     * This method will release the script executor to the pool
     */
    public static void releaseScriptExecutor(Executor executor) throws InterruptedException {
        executorPool.put(executor);
    }
}
