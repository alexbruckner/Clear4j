package clear4j.config;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.WorkflowProcessor;

// this is just a helper class that has predefined workflows to run.
public class Workflows {


    public static Workflow getMonitorWorkflow(String host, int port){
        return new Workflow(new Function(WorkflowProcessor.class, new HostPort(host, port), "monitor"));
    }


}
