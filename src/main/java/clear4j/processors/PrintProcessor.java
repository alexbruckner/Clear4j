package clear4j.processors;

import clear4j.processor.Arg;
import clear4j.processor.Function;

import java.util.Arrays;


public class PrintProcessor {

//	@Function    //TODO adding this method should not break the test
//	public void println(){
//		System.out.println();
//	}
	
	@Function
	public Object println(Object value){
        System.out.println(value);
        return value;
	}
	
	//extra parameters
	@Function
	public Object println(Object value, Arg<?>... args){
		System.out.println(String.format("Extra Args: [%s]", Arrays.toString(args)));
		return println(value);
	}
	
}
