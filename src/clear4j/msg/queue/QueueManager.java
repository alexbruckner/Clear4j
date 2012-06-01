package clear4j.msg.queue;

import clear4j.msg.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 15:08
 */
public final class QueueManager {
    private QueueManager(){}

    private static final Logger LOG = Logger.getLogger(QueueManager.class.getName());

    private static final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Message>> messages = new ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Message>>();
    private static final ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Receiver>> receivers = new ConcurrentHashMap<Queue, ConcurrentLinkedQueue<Receiver>>();

    private static final Object lock = new Object();

    private static volatile boolean working = false;
    private static volatile boolean runagain = false;

    static {
        start();
    }

    /*
     * SENDING
     */

    public static void add(Message message) {
        Queue name = message.getQueue();
        ConcurrentLinkedQueue<Message> queue = messages.get(name);
        if (queue == null) {
            createMessageQueue(name);
            queue = messages.get(name);
        }
        if (LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, String.format("Adding message [%s]", message));
        }
        queue.add(message);

        synchronized (lock){
            if (!working){
                runagain = false;
            } else {
                runagain = true;
            }
            lock.notifyAll();
        }
    }

    private static void createMessageQueue(Queue name) {
        if (!messages.contains(name)) {
            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("Creating message queue [%s]", name));
            }
            messages.put(name, new ConcurrentLinkedQueue<Message>());
        }
    }

    /*
     * RECEIVING
     */

    public static void add(Receiver receiver) {
        Queue name = receiver.getQueue();
        ConcurrentLinkedQueue<Receiver> queue = receivers.get(name);
        if (queue == null) {
            createReceiverQueue(name);
            queue = receivers.get(name);
        }
        if (LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, String.format("Adding receiver [%s]", receiver));
        }
        queue.add(receiver);
    }

    private static void createReceiverQueue(Queue name) {
        if (!receivers.contains(name)) {
            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("Creating receiver queue [%s]", name));
            }
            receivers.put(name, new ConcurrentLinkedQueue<Receiver>());
        }
    }

    /*
     * RECEIVER THREAD ACCESS
     */
    public static void start(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try{
                        working = true;
                        for (Map.Entry<Queue, ConcurrentLinkedQueue<Message>> entry : messages.entrySet()){
                            final Queue name = entry.getKey();
                            final ConcurrentLinkedQueue<Message> queue = entry.getValue();
                            while (!queue.isEmpty()) {
                                final Message message = queue.poll();
                                ConcurrentLinkedQueue<Receiver> receiverQueue = receivers.get(name);
                                if (receiverQueue != null) {
                                    for (Receiver receiver : receiverQueue){
                                        callReceiver(receiver, message);
                                    }
                                }
                            }
                        }
                        working = false;
                        //wait for a new message to arrive, before processing the queues again

                        synchronized (lock) {
                            if (!runagain){
                                lock.wait();
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            private void callReceiver(final Receiver receiver, final Message message) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        receiver.onMessage(message);
                    }
                }).start();
            }

        }).start();
    }

}
