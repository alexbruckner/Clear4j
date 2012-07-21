package clear4j;

import clear4j.msg.queue.Host;
import clear4j.processors.FileProcessor;
import clear4j.processors.FinalProcessor;
import clear4j.processors.PrintProcessor;

@Config
public class Functions {
	
	public static FunctionDefinition loadText(){
		return new FunctionDefinition(FileProcessor.class, Host.LOCAL_HOST, "loadText");
	}
	
	public static FunctionDefinition println(){
		return new FunctionDefinition(PrintProcessor.class, Host.LOCAL_HOST, "println");
	}
	
	public static FunctionDefinition finalProcess(){
		return new FunctionDefinition(FinalProcessor.class, Host.LOCAL_HOST, "finalProcess");
	}
}
