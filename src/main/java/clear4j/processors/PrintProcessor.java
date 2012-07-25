package clear4j.processors;

import clear4j.processor.Arg;
import clear4j.processor.Function;

import java.util.Arrays;


public class PrintProcessor {
	
	@Function
	public Object println(Object value){
        System.out.println(String.format("PRINTING: %s", value));
        return value;
	}
	
	//extra parameters
	@Function
	public Object println(Object value, Arg<?>... args){
		System.out.println(String.format("Extra Args: [%s]", Arrays.toString(args)));
		return println(value);
	}
	
}
