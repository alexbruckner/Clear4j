package clear4j;

import clear4j.msg.queue.Host;
import clear4j.processors.FileProcessor;
import clear4j.processors.FinalProcessor;
import clear4j.processors.PrintProcessor;
import clear4j.processors.ThrowExceptionProcessor;

@Config
public class Functions {
	
	public static Function loadText(){
		return new Function(FileProcessor.class, Host.LOCAL_HOST, "loadText");
	}
	
	public static Function println(){
		return new Function(PrintProcessor.class, Host.LOCAL_HOST, "println");
	}
	
	public static Function finalProcess(){
		return new Function(FinalProcessor.class, Host.LOCAL_HOST, "finalProcess");
	}
	
	public static Function throwRuntimeException(){
		return new Function(ThrowExceptionProcessor.class, Host.LOCAL_HOST, "throwRuntimeException");
	}
	
	public static Function throwCheckedException(){
		return new Function(ThrowExceptionProcessor.class, Host.LOCAL_HOST, "throwCheckedException");
	}
	
}
