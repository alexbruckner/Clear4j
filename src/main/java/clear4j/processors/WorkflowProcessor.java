package clear4j.processors;

import clear4j.Runner;
import clear4j.beans.Workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WorkflowProcessor {

    private static final Logger LOG = Logger.getLogger(WorkflowProcessor.class.getName());

    // LOCKS for waitFor method
    private static final Map<String, Object> LOCKS = new ConcurrentHashMap<String, Object>();
    // Workflows returned that have been waited for
    private static final Map<String, Workflow> RECEIVED = new ConcurrentHashMap<String, Workflow>();
    // Active workflows
    private static final List<Workflow> ACTIVE_WORKFLOWS = new CopyOnWriteArrayList<Workflow>();

    public void initialProcess(Workflow workflow){
        if (LOG.isLoggable(Level.INFO)){
            LOG.info(String.format("Running new workflow [%s]", workflow.getId()));
        }
        ACTIVE_WORKFLOWS.add(workflow);
    }

	public void finalProcess(Workflow workflow){

        if (LOG.isLoggable(Level.INFO)){
            LOG.info(String.format("Finishing workflow [%s]", workflow.getId()));
        }
        if (LOCKS.containsKey(workflow.getId())){
            RECEIVED.put(workflow.getId(), workflow);
        }
		Object lock = LOCKS.get(workflow.getId());
		if (lock != null){
			synchronized (lock) {
				lock.notifyAll();
			}
		}

        ACTIVE_WORKFLOWS.remove(workflow);   //TODO for now we retain all workflows that were started locally.

	}

    public void runWorkflowRemotely(Workflow workflow){
    	Workflow localized = Workflow.localize(workflow);
    	Runner.run(localized, localized.getNextInstruction());
    }

    public List<Workflow> monitor(){
    	synchronized(ACTIVE_WORKFLOWS){
    		return new ArrayList<Workflow>(ACTIVE_WORKFLOWS);
    	}
    }

	public static Serializable waitFor(String id){
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
        Workflow workflow = RECEIVED.remove(id);
        return workflow.getCurrentInstruction().getValue();
	}
}