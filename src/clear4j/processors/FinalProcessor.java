package clear4j.processors;

import clear4j.Workflow;
import clear4j.processor.Process;
import clear4j.processor.Processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Processor
public class FinalProcessor {

	private static final Map<String, Workflow> WORK_DONE = new ConcurrentHashMap<String, Workflow>();
	
	@Process
	public void finalProcess(Workflow workflow){
		synchronized(workflow){
			WORK_DONE.put(workflow.getId(), workflow);
			workflow.notifyAll();
		}
	}
	
	public static Workflow getWorkflow(String id){
		return WORK_DONE.get(id);
	}
}
