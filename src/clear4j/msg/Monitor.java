package clear4j.msg;

import clear4j.msg.queue.management.QueueManager;
import clear4j.msg.queue.monitor.Callback;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.Set;

public class Monitor implements Runnable {
    private final int frequency; //milliseconds
    private boolean running = true;
    private Thread monitorThread;
    private Callback callback;

    public Monitor(final Callback callback) {
        this(5000, callback);
    }

    public Monitor(final int frequency, final Callback callback) {
        this.frequency = 5000;
        this.callback = callback;
    }

    /*
    * Monitor method
    */

    private <T extends Serializable> void monitor(){
        Set<QueueStatus> status = QueueManager.status();
        callback.call(status);
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

}
