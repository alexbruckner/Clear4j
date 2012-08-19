package clear4j.config;

import clear4j.beans.Function;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processor.Param;
import clear4j.processors.PrintProcessor;
import clear4j.processors.ThrowExceptionProcessor;


public class TestFunctions {

	public static Function remotePrintln(Param... params) {
        return new Function(new HostPort(Host.LOCAL_HOST.getHost(), 7777), PrintProcessor.class, "println", Object.class, params);
    }

	public static Function printNewLineOnly(){
		return new Function(PrintProcessor.class, "println");
	}

	public static Function println(Param... params){
		return new Function(PrintProcessor.class, "println", Object.class, params);
	}

	public static Function throwRuntimeException(){
		return new Function(ThrowExceptionProcessor.class, "throwRuntimeException");
	}

	public static Function throwCheckedException(){
		return new Function(ThrowExceptionProcessor.class,"throwCheckedException");
	}
}
