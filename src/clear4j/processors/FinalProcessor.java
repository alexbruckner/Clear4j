package clear4j.processors;

import clear4j.processor.Key;
import clear4j.processor.Process;


public class FinalProcessor {
	
	@Process("finalized")
	public boolean process(@Key("id") String messageId){ 
		return true;
	}
	
}
