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
	
//	@Function //TODO
//	public Object println(Object value, @Param("key1") String param1, @Param("key2") String param2){
//		System.out.println(String.format("Extra Args: Key1: [%s], Key2: [%s]", param1, param2));
//		return println(value);
//	}

	//extra parameters
	@Function
	public Object println(Object value, Param... args){
		System.out.println(String.format("Extra Args: [%s]", Arrays.toString(args)));
		return println(value);
	}
}
