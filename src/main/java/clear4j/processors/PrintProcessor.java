package clear4j.processors;

import clear4j.processor.Function;


public class PrintProcessor {
	
	@Function
	public Object println(Object value){
        System.out.println(String.format("PRINTING: %s", value));
        return value;
	}
	
}
