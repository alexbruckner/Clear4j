package clear4j.msg;

import clear4j.msg.queue.managers.QueueManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;
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

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

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
        private String host;
        private int port;

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
        public Adapter on(String host, int port) {
            this.host = host;
            this.port = port;
            return new Adapter(this);
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

    private static void register(Receiver receiver) {
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

    /*
     * implicitly creates a Receiver to receive one message from a queue
     * primarily for testing otherwise use Messenger.register(clear4j.msg.Receiver).to(queue);
     */
    public static Future<clear4j.msg.Message> register(final String queue) {

        final CountDownLatch latch = new CountDownLatch(1);
        final clear4j.msg.Message[] returned = new clear4j.msg.Message[1];

        final Receiver receiver = register(new clear4j.msg.Receiver() {
            @Override
            public void onMessage(clear4j.msg.Message message) {
                System.out.println(message + "!!!!!!!!!!!!!!!!!!!");
                returned[0] = message;
                latch.countDown();
            }
        }).to(queue);

        FutureTask<clear4j.msg.Message> futureTask = new FutureTask<clear4j.msg.Message>(
            new Callable<clear4j.msg.Message>() {
                @Override
                public clear4j.msg.Message call() throws InterruptedException {
                    latch.await();
                    unregister(receiver);
                    return returned[0];
                }
            }
        );

        executor.execute(futureTask);

        return futureTask;

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

    private static class Adapter implements clear4j.msg.queue.Adapter {

        private final Message message;

        private Adapter(Message message) {
            this.message = message;
        }

        public void to(String queue) {
            message.queue = queue;
            try {
                Socket socket = new Socket(message.host, message.port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                out.writeObject(message);
                out.flush();
                out.close();
                socket.close();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage());
            }
        }

    }
}
