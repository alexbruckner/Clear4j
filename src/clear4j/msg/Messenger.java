package clear4j.msg;

import clear4j.msg.queue.managers.QueueManager;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:41
 */
public final class Messenger {
    private Messenger() {
    }

    private static final Logger LOG = Logger.getLogger(Messenger.class.getName());

    private static final Object LOCK = new Object();

    /*
     * SENDING
     */

    public static void send(clear4j.msg.Message message) {
        QueueManager.add(message);
    }

    public static clear4j.msg.Message send(String message) {
        return Messenger.newMessage(message);
    }

    public static Message newMessage(String message) {
        return new Message(message);
    }

    public static void waitFor(String name) {
        // register temporary receiver and send a message. once we get it back,
        // we know that all previous messages should have been dealt with.
        final Message waitForMessage = new Message(String.format("<waitFor queue=\"%s\"/>", name));

        final boolean[] received = {false};

        //register
        Receiver receiver = Messenger.register(new clear4j.msg.Receiver() {
            @Override
            public void onMessage(clear4j.msg.Message message) {
                if (message.getId() == waitForMessage.getId()) {
                    received[0] = true;
                    synchronized (LOCK) {
                        LOCK.notifyAll();
                    }
                }
            }
        }).to(name);

        //send
        waitForMessage.to(name);

        //wait
        while(!received[0]) {
            synchronized (LOCK) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        //unregister
        Messenger.unregister(receiver);
    }

    private static class Message implements clear4j.msg.Message {
        private final String message;
        private String queue;
        private final long id;
        private final static AtomicLong count = new AtomicLong();

        private Message(String message) {
            this.message = message;
            this.id = count.addAndGet(1);
        }

        public String getMessage() {
            return message;
        }

        @Override
        public Receiver to(String queue) {
            this.queue = queue;
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("sending [%s]", this));
            }
            send(this);
            return null;
        }

        @Override
        public String getQueue() {
            return queue;
        }

        public long getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", queue=" + queue +
                    '}';
        }
    }

    /*
     * RECEIVING
     */

    public static void register(Receiver receiver) {
        if (receiver.getQueue() == null) {
            throw new RuntimeException("Receiver needs a queue");
        }
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("registering receiver: %s", receiver));
        }
        QueueManager.add(receiver);
    }

    public static Receiver register(clear4j.msg.Receiver callback) {
        return new Receiver(callback);
    }

    public static void unregister(Receiver receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("un-registering receiver: %s", receiver));
        }
        QueueManager.remove(receiver);
    }

    public static class Receiver implements clear4j.msg.queue.Receiver {
        private final clear4j.msg.Receiver callback;
        private String queue;

        private Receiver(clear4j.msg.Receiver callback) {
            this.callback = callback;
        }

        @Override
        public Receiver to(String queue) {
            this.queue = queue;
            register(this);
            return this;
        }

        @Override
        public String getQueue() {
            return queue;
        }

        @Override
        public void onMessage(clear4j.msg.Message message) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("onMessage [%s]", message));
            }
            callback.onMessage(message);
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("called on message"));
            }
        }

        @Override
        public String toString() {
            return "Receiver{" +
                    "callback=" + callback +
                    ", queue=" + queue +
                    '}';
        }
    }
}
