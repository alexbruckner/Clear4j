package clear4j.monitor;

import clear4j.processors.WorkflowProcessor;

public class WorkflowMonitor extends Thread {


    @Override
    public void run(){
        while (!isInterrupted()){
            System.out.println(WorkflowProcessor.getActiveWorkflows());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public static void main(String[] args) {
        new WorkflowMonitor().start();
    }


}
