package clear4j.msg.queue.management;


import clear4j.msg.queue.Message;
import clear4j.msg.queue.QueueManagement;
import clear4j.msg.queue.Receiver;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 15:08
 */
public final class QueueManager {
    private QueueManager() {}

    private static final Logger LOG = Logger.getLogger(QueueManager.class.getName());
    private static final QueueManagement QUEUE_MANAGER = new BlockingQueueManager();

    static {
        //start remote adapter
        new RemoteAdapter();
    }

    /*
     * SENDING
     */

    public static <T extends Serializable> void add(Message<T> message) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Adding message [%s]", message));
        }
        
        QUEUE_MANAGER.add(message);
        
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Added message [%s]", message));
        }
    }

    /*
     * RECEIVING
     */

    public static <T extends Serializable> void add(Receiver<T> receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Adding receiver [%s]", receiver));
        }
        QUEUE_MANAGER.add(receiver);
    }

    public static <T extends Serializable> void remove(Receiver<T> receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Removing receiver [%s]", receiver));
        }
        QUEUE_MANAGER.remove(receiver);
    }

}
