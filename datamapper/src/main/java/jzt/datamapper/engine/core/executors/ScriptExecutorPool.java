
package jzt.datamapper.engine.core.executors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScriptExecutorPool {

    private BlockingQueue<Executor> executors;

    public ScriptExecutorPool(ScriptExecutorType executorType, int executorPoolSize) {
        executors = new LinkedBlockingQueue<>();
        for (int i = 0; i < executorPoolSize; i++) {
            Executor executor = createScriptExecutor(executorType);
            if (executor != null) {
                executors.add(executor);
            }
        }
    }

    private Executor createScriptExecutor(ScriptExecutorType executorType) {
        return new ScriptExecutor(executorType);
    }

    public Executor take() throws InterruptedException {
        return executors.take();
    }

    public void put(Executor executor) throws InterruptedException {
        executors.put(executor);
    }
}
