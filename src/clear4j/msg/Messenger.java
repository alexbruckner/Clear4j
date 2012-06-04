package clear4j.msg;

import clear4j.msg.queue.Queue;
import clear4j.msg.queue.QueueManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:41
 */
public final class Messenger {
    private Messenger(){}

    private static final Logger LOG = Logger.getLogger(Messenger.class.getName());


    /*
     * SENDING
     */

    public static void send(clear4j.msg.Message message){
        QueueManager.add(message);
    }

    public static clear4j.msg.Message send(String message){
        return Messenger.newMessage(message);
    }

    public static Message newMessage(String message){
        return new Message(message);
    }

    private static class Message implements clear4j.msg.Message {
        private final String message;
        private Queue queue;

        private Message(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public Receiver to(Queue queue) {
            this.queue = queue;
            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("sending [%s]", this));
            }
            send(this);
            return null;
        }

        @Override
        public Queue getQueue() {
            return queue;
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
        QueueManager.add(receiver);
    }

    public static Receiver register(clear4j.msg.Receiver callback){
        return new Receiver(callback);
    }

    public static class Receiver implements clear4j.msg.queue.Receiver {
        private final clear4j.msg.Receiver callback;
        private Queue queue;
        private final Object lock = new Object();

        private Receiver(clear4j.msg.Receiver callback){
            this.callback = callback;
        }

        @Override
        public Receiver to(Queue queue) {
            this.queue = queue;
            register(this);
            return this;
        }

        @Override
        public Queue getQueue() {
            return queue;
        }

        @Override
        public void onMessage(clear4j.msg.Message message) {
            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("onMessage [%s]", message));
            }
            callback.onMessage(message);
            synchronized (lock) {
                lock.notifyAll();
            }
            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("called on message"));
            }
        }

        public void waitForOneMessage(){
            waitFor(1);
        }

        public void waitFor(int numberOfMessages) {
            for (int i = 0; i < numberOfMessages; i++){
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }


}
