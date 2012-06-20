package clear4j.msg.queue.managers;


import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

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
    private static final QueueManagement QUEUE_MANAGER = new QueueManagerImpl();

    /*
     * SENDING
     */

    public static void add(Message message) {
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

    public static void add(Receiver receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Adding receiver [%s]", receiver));
        }
        QUEUE_MANAGER.add(receiver);
    }

    public static void remove(Receiver receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Removing receiver [%s]", receiver));
        }
        QUEUE_MANAGER.remove(receiver);
    }

}
