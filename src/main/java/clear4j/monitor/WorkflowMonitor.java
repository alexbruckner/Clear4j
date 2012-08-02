package clear4j.monitor;

import clear4j.Clear;
import clear4j.config.Workflows;
import clear4j.msg.queue.management.RemoteAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class WorkflowMonitor extends Thread {
	
    private static final Logger LOG = Logger.getLogger(RemoteAdapter.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	    

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
