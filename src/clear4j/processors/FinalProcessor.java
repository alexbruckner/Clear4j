package clear4j.processors;

import clear4j.processor.Process;
import clear4j.processor.Value;


public class FinalProcessor {
	
	@Process
	public boolean process(@Value("id") String messageId){
		return true;
	}
	
}
