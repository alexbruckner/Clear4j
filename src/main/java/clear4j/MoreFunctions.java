package clear4j;

import clear4j.msg.queue.Host;
import clear4j.processors.PrintProcessor;

public class MoreFunctions {
    public static Function println(){
		return new Function(PrintProcessor.class, Host.LOCAL_HOST, "println");
	}
}
