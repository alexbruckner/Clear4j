package clear4j.processors;

import java.util.Arrays;

import clear4j.processor.Arg;
import clear4j.processor.Function;


public class PrintProcessor {
	
	@Function
	public Object println(Object value){
        System.out.println(String.format("PRINTING: %s", value));
        return value;
	}
	
	//extra parameters
	// TODO check method overloading would work too.
	@Function
	public Object printlnWithArgs(Object value, Arg<?>... args){
		System.out.println(String.format("Extra Args: [%s]", Arrays.toString(args)));
		return println(value);
	}
	
}
