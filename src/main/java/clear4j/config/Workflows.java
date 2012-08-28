package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.WorkflowProcessor;


// this is just a helper class that has predefined workflows to run.
public class Workflows {


    public static Workflow getMonitorWorkflow(){
    	Workflow monitorWorkflow = new Workflow(new Function(WorkflowProcessor.class, "monitor"));
    	monitorWorkflow.setName("Monitor");
    	return monitorWorkflow;
    }

    /*
     * starts and finishes a workflow on a remote machine (the individual instructions are still being sent to where they should go.)
     */
    public static Workflow runWorkflowRemotely(String host, int port, Workflow workflow){
        return new Workflow(workflow, new Function(new HostPort(host, port), WorkflowProcessor.class, "runWorkflowRemotely", Workflow.class));
    }

}
