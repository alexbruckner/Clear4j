package clear4j.processors;

import clear4j.processor.Process;


public class PrintProcessor {
	
	@Process
	public void println(String str){
        System.out.println(String.format("PRINTING: %s",str));
	}
	
}
