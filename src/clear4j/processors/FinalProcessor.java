package clear4j.processors;

import clear4j.Workflow;
import clear4j.processor.Process;
import clear4j.processor.Processor;

import java.util.Map;


@Processor
public class FinalProcessor {

    //TODO final message store there to allow for waitFor methods?

	@Process
	public void finalProcess(Workflow workflow){
		System.out.println(String.format("FINAL PROCESSOR: %s", workflow));
	}
	
}
