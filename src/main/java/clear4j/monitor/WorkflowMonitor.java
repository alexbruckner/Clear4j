package clear4j.monitor;

import clear4j.Clear;
import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.config.Workflows;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.PrintProcessor;

import java.util.logging.LogManager;

public class WorkflowMonitor extends Thread {

    static {
        LogManager.getLogManager().reset(); //turn off logging
    }

    private final String host;
    private final int port;

    public WorkflowMonitor(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run(){
        while (!isInterrupted()){
            try {
                Clear.run(Workflows.getMonitorWorkflow(host, port)).waitFor();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
    	System.out.println("connecting to localhost:7777");
        new WorkflowMonitor("localhost", 7777).start();
        Clear.run(new Workflow("test", new Function(PrintProcessor.class, new HostPort(Host.LOCAL_HOST.getHost(), 7777), "println")));
    }

}
