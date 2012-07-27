package clear4j.monitor;

public class WorkflowMonitor extends Thread {


    @Override
    public void run(){
        while (!isInterrupted()){
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
