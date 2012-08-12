package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.beans.AbstractRemoteOrigin;
import clear4j.msg.queue.Host;
import clear4j.processor.Arg;
import clear4j.processors.*;

@Config
public class Functions {

	public static Function loadText(){
		return new Function(FileProcessor.class, "loadText", String.class);
	}
	
	public static Function println(){
		return new Function(PrintProcessor.class, "println", Object.class);
	}

	public static Function println(Arg... args){
		return new Function(PrintProcessor.class, "println", Object.class, args);
	}

	public static Function throwRuntimeException(){
		return new Function(ThrowExceptionProcessor.class, "throwRuntimeException", null);
	}
	
	public static Function throwCheckedException(){
		return new Function(ThrowExceptionProcessor.class,"throwCheckedException", null);
	}

    /*
     * SPECIALS
     */

    public static Function initialProcess() {
        return new Function(WorkflowProcessor.class, "initialProcess", Workflow.class);
    }

    public static Function finalProcess(){
        return new Function(WorkflowProcessor.class, "finalProcess", Workflow.class);
    }

    /*
     * MORE SPECIALS
     */

    public static Function monitor() {
        return new Function(WorkflowProcessor.class, "monitor", null);
    }

    public static Function sleep() {
        return new Function(SleepProcessor.class, "sleep", Object.class );
    }

}
