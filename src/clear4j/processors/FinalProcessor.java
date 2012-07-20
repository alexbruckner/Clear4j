package clear4j.processors;

import clear4j.processor.Process;
import clear4j.processor.Processor;


@Processor
public class FinalProcessor {

    //TODO final message store there to allow for waitFor methods?

	@Process
	public Object finalProcess(Object value){
        return value;
	}
	
}
