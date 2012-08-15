package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.processor.Param;
import clear4j.processors.*;

@Config
public class Functions {

	public static Function loadText(){
		return new Function(FileProcessor.class, "loadText", String.class);
	}

    //temporary test method here //TODO move elsewhere
    public static Function printNewLineOnly(){
        return new Function(PrintProcessor.class, "println", null);    //TODO remove need to pass null in?
    }

	public static Function println(){
		return new Function(PrintProcessor.class, "println", Object.class);
	}

	public static Function println(Param... params){
		return new Function(PrintProcessor.class, "println", Object.class, params);
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
