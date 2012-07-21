package clear4j;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.FileProcessor;
import clear4j.processors.FinalProcessor;
import clear4j.processors.PrintProcessor;

@Config
public class Functions {
	
	public static Function loadText(){
		return new Function(FileProcessor.class, Host.LOCAL_HOST, "loadText");
	}
	
	public static Function println(){ //TODO
		return new Function(PrintProcessor.class, new HostPort(Host.LOCAL_HOST.getHost(), 7777), "println");
	}
	
	public static Function finalProcess(){
		return new Function(FinalProcessor.class, Host.LOCAL_HOST, "finalProcess");
	}
}
