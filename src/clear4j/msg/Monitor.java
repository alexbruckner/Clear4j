package clear4j.msg;

import clear4j.msg.queue.management.QueueManager;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.Set;

public class Monitor implements Runnable {
    private static final int frequency = 5000; //milliseconds
    private boolean running = true;
    private Thread monitorThread;
    /*
     * Monitor method
     */

    private <T extends Serializable> void monitor(){
        Set<QueueStatus<T>> status = QueueManager.status();
        for (QueueStatus<T> queueStatus : status){
            System.out.printf("%s(%d)%n", queueStatus.getQueue(), queueStatus.getReceivers().size());
        }

    }

    /*
     * Thread methods
     */

    @Override
    public void run() {
        try {
            while (running) {
                monitor();
                Thread.sleep(frequency);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        if (running) {
            monitorThread = new Thread(this);
            monitorThread.start();
        }
    }

    public void stop() {
        if (running) {
            running = false;
            monitorThread.interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Monitor().start();
    }
}
