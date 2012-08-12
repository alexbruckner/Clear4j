package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.PrintProcessor;
import clear4j.processors.SleepProcessor;
import clear4j.processors.WorkflowProcessor;

import java.io.Serializable;

// this is just a helper class that has predefined workflows to run.
public class Workflows {


    public static Workflow getMonitorWorkflow(){
    	Workflow monitorWorkflow = new Workflow(new Function(WorkflowProcessor.class, "monitor", null));
    	monitorWorkflow.setName("Monitor");
    	return monitorWorkflow;
    }

    /*
     * starts and finishes a workflow on a remote machine (the individual instructions are still being sent to where they should go.)
     */
    public static Workflow runWorkflowRemotely(String host, int port, Workflow workflow){
        return new Workflow(workflow, new Function[]{new Function(new HostPort(host, port), WorkflowProcessor.class, "runWorkflowRemotely", Workflow.class)});
    }

    public static Workflow remotePrintlnAndSleep( String host, int port, Serializable value){
        return new Workflow(value, new Function[]{new Function(new HostPort(host, port), PrintProcessor.class, "println", Object.class), new Function(new HostPort(host, port), SleepProcessor.class, "sleep", Object.class)});
    }

}
