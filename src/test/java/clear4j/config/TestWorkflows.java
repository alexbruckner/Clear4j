package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.PrintProcessor;
import clear4j.processors.SleepProcessor;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 19/08/12
 * Time: 13:39
 */
public class TestWorkflows {
	public static Workflow remotePrintlnAndSleep(String host, int port, Serializable value){
		return new Workflow(value, new Function[]{new Function(new HostPort(host, port), PrintProcessor.class, "println", Object.class), new Function(new HostPort(host, port), SleepProcessor.class, "sleep", Object.class)});
	}
}
