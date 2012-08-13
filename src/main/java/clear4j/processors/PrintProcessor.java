package clear4j.processors;

import clear4j.processor.Param;
import clear4j.processor.annotations.Function;

import java.util.Arrays;


public class PrintProcessor {

	@Function
	public void println(){
		System.out.println();
	}
	
	@Function
	public Object println(Object value){
        System.out.println(value);
        return value;
	}
	
	//extra parameters
	@Function
	public Object println(Object value, Param<?>... params){
		System.out.println(String.format("Extra Args: [%s]", Arrays.toString(params)));
		return println(value);
	}
	
}
