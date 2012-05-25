package clear4j.msg.queue;

import clear4j.msg.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 15:08
 */
public class QueueManager {

    private static final Logger LOG = Logger.getLogger(QueueManager.class.getName());

    private static final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Message>> container = new ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Message>>();


    public static void add(Message message) {
        Queue name = message.getQueue();
        ConcurrentLinkedQueue<Message> queue = container.get(name);
        if (queue == null) {
            createQueue(name);
            queue = container.get(name);
        }
        if (LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, String.format("Adding message [%s]", message));
        }
        queue.add(message);
    }


    private static void createQueue(Queue name) {
        if (!container.contains(name)) {
            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("Creating queue [%s]", name));
            }
            container.put(name, new ConcurrentLinkedQueue<Message>());
        }
    }

}
