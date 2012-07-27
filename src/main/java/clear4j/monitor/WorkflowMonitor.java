package clear4j.monitor;

import clear4j.Clear;
import clear4j.config.Workflows;

public class WorkflowMonitor extends Thread {

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
                System.out.println("--->"+Clear.run(Workflows.getMonitorWorkflow(host, port)).waitFor());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new WorkflowMonitor("localhost", 9876).start();
    }

}
