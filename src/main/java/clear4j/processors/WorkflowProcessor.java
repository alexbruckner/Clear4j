package clear4j.processors;

import clear4j.Workflow;
import clear4j.processor.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WorkflowProcessor {

    private static final Logger LOG = Logger.getLogger(WorkflowProcessor.class.getName());


    private static final Map<String, Object> LOCKS = new ConcurrentHashMap<String, Object>();

    @Function
    public void initialProcess(Workflow workflow){
        if (LOG.isLoggable(Level.INFO)){
            LOG.info(String.format("Running new workflow [%s]", workflow.getId()));
        }
    }

    @Function
	public void finalProcess(Workflow workflow){
        if (LOG.isLoggable(Level.INFO)){
            LOG.info(String.format("Finishing workflow [%s]", workflow.getId()));
        }
		Object lock = LOCKS.get(workflow.getId());
		if (lock != null){
			synchronized (lock) {
				lock.notifyAll();
			}
		}
		
	}

	public static void waitFor(String id){
		//create LOCK object for id
		Object lock = new Object();
		LOCKS.put(id, lock);
		synchronized(lock){
			try {
				lock.wait();
				LOCKS.remove(id);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
}
