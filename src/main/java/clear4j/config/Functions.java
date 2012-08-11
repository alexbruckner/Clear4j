package clear4j.config;

import clear4j.beans.Function;
import clear4j.msg.queue.Host;
import clear4j.processors.*;

@Config
public class Functions {
	
	public static Function loadText(){
		return new Function(FileProcessor.class, "loadText");
	}
	
	public static Function println(){
		return new Function(PrintProcessor.class, "println");
	}

	public static Function throwRuntimeException(){
		return new Function(ThrowExceptionProcessor.class, "throwRuntimeException");
	}
	
	public static Function throwCheckedException(){
		return new Function(ThrowExceptionProcessor.class,"throwCheckedException");
	}

    /*
     * SPECIALS
     */

    public static Function initialProcess() {
        return new Function(WorkflowProcessor.class, "initialProcess");
    }

    public static Function finalProcess(){
        return new Function(WorkflowProcessor.class, "finalProcess");
    }

    /*
     * MORE SPECIALS
     */

    public static Function monitor() {
        return new Function(WorkflowProcessor.class, "monitor");
    }

    public static Function sleep() {
        return new Function(SleepProcessor.class, "sleep");
    }

}
