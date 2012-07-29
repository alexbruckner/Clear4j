package clear4j.monitor;

import clear4j.Clear;
import clear4j.config.Workflows;

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
