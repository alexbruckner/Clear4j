package clear4j.processors;

import clear4j.processor.Return;
import clear4j.processor.Value;


public class FinalProcessor {
	
	@Return("finalized")
	public boolean process(@Value("id") String messageId){
		return true;
	}
	
}
