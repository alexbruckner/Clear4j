package clear4j.processors;

import clear4j.processor.Process;


public class PrintProcessor {
	
	@Process
	public Object println(Object value){
        System.out.println(String.format("PRINTING: %s", value));
        return value;
	}
	
}
