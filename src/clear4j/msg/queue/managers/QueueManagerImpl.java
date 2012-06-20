package clear4j.msg.queue.managers;

import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueManagerImpl implements QueueManagement {


    private static final Logger LOG = Logger.getLogger(QueueManager.class.getName());

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>> messages = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Message>>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Receiver>> receivers = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Receiver>>();

    private final Object receiverLock = new Object();   // for queue iterations themselves
    private final Object waitForLock = new Object();  // for checking queue state (ie. empty)

    private volatile boolean working = false;
    private volatile boolean runagain = false;
    
    QueueManagerImpl() {
    	start();
    }

    /*
     * SENDING
     */

    public void add(Message message) {

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("getting queue"));
        }
        String name = message.getQueue();
        ConcurrentLinkedQueue<Message> queue = messages.get(name);
        if (queue == null) {
            createMessageQueue(name);
            queue = messages.get(name);
        }

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("Adding message."));
        }

        queue.add(message);

        synchronized (receiverLock) {
            runagain = working;
            receiverLock.notifyAll();
        }

    }

    private void createMessageQueue(String name) {
        if (!messages.contains(name)) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("Creating message queue [%s]", name));
            }
            messages.put(name, new ConcurrentLinkedQueue<Message>());
        }
    }

    /*
     * RECEIVING
     */

    public void add(Receiver receiver) {
        String name = receiver.getQueue();
        ConcurrentLinkedQueue<Receiver> queue = receivers.get(name);
        if (queue == null) {
            createReceiverQueue(name);
            queue = receivers.get(name);
        }

        queue.add(receiver);
    }

    public void remove(Receiver receiver) {
        String name = receiver.getQueue();
        ConcurrentLinkedQueue<Receiver> queue = receivers.get(name);
        if (!queue.remove(receiver)) {
            LOG.log(Level.SEVERE, "Could not remove %s", receiver);
        }
    }

    private void createReceiverQueue(String name) {
        if (!receivers.contains(name)) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("Creating receiver queue [%s]", name));
            }
            receivers.put(name, new ConcurrentLinkedQueue<Receiver>());
        }
    }

    /*
     * RECEIVER THREAD ACCESS
     */
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        working = true;
                        for (Map.Entry<String, ConcurrentLinkedQueue<Message>> entry : messages.entrySet()) {
                            final String name = entry.getKey();
                            final ConcurrentLinkedQueue<Message> queue = entry.getValue();
                            while (!queue.isEmpty()) {
                                final Message message = queue.poll();
                                ConcurrentLinkedQueue<Receiver> receiverQueue = receivers.get(name);
                                if (receiverQueue != null) {
                                    for (Receiver receiver : receiverQueue) {
                                        callReceiver(receiver, message);
                                    }
                                }
                            }
                        }

                        // unlock threads waiting for empty queues
                        synchronized (waitForLock) {
                            waitForLock.notifyAll();
                        }

                        //wait for a new message to arrive, before processing the queues again
                        synchronized (receiverLock) {
                            working = false;
                            if (!runagain) {
                                receiverLock.wait();
                            }
                        }
                    } catch (Exception e) {
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
