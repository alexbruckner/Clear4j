package clear4j.config;

import clear4j.beans.Function;
import clear4j.msg.queue.Host;
import clear4j.processors.*;

@Config
public class Functions {
	
	public static Function loadText(){
		return new Function(FileProcessor.class, Host.LOCAL_HOST, "loadText");
	}
	
	public static Function println(){
		return new Function(PrintProcessor.class, Host.LOCAL_HOST, "println");
	}

	public static Function throwRuntimeException(){
		return new Function(ThrowExceptionProcessor.class, Host.LOCAL_HOST, "throwRuntimeException");
	}
	
	public static Function throwCheckedException(){
		return new Function(ThrowExceptionProcessor.class, Host.LOCAL_HOST, "throwCheckedException");
	}

    /*
     * SPECIALS
     */

    public static Function initialProcess() {
        return new Function(WorkflowProcessor.class, Host.LOCAL_HOST, "initialProcess");
    }

    public static Function finalProcess(){
        return new Function(WorkflowProcessor.class, Host.LOCAL_HOST, "finalProcess");
    }

    /*
     * MORE SPECIALS
     */

    public static Function monitor() {
        return new Function(WorkflowProcessor.class, Host.LOCAL_HOST, "monitor");
    }

    public static Function sleep() {
        return new Function(SleepProcessor.class, Host.LOCAL_HOST, "sleep");
    }

}
