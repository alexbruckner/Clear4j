package clear4j.processors;

import clear4j.processor.Process;
import clear4j.processor.Processor;


@Processor
public class FinalProcessor {
	
	@Process
	public Object finalProcess(Object value){
        return value;
	}
	
}
