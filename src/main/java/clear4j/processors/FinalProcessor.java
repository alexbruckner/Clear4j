package clear4j.processors;

import clear4j.Workflow;
import clear4j.processor.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FinalProcessor {

	private static final Map<String, Object> LOCKS = new ConcurrentHashMap<String, Object>();
	
	@Function
	public void finalProcess(Workflow workflow){
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
