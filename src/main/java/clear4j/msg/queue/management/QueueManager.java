package clear4j.msg.queue.management;


import clear4j.msg.queue.Message;
import clear4j.msg.queue.QueueManagement;
import clear4j.msg.queue.Receiver;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.Set;
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
    @SuppressWarnings("rawtypes")
	private static final QueueManagement QUEUE_MANAGER = new BlockingQueueManager();

    static {
        //start remote adapter
        new RemoteAdapter();
    }

    /*
     * SENDING
     */

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
	public static <T extends Serializable> void add(Receiver<T> receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Adding receiver [%s]", receiver));
        }
        QUEUE_MANAGER.add(receiver);
    }

    @SuppressWarnings("unchecked")
	public static <T extends Serializable> void remove(Receiver<T> receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Removing receiver [%s]", receiver));
        }
        QUEUE_MANAGER.remove(receiver);
    }

    /*
     * MONITORING
     */
    @SuppressWarnings("unchecked")
	public static <T extends Serializable> Set<QueueStatus> status(){
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "Returning status of all queues...");
        }
        return QUEUE_MANAGER.status();
    }

}
