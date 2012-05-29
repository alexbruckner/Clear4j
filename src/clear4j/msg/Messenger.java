package clear4j.msg;

import clear4j.msg.queue.Queue;
import clear4j.msg.queue.QueueManager;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:41
 */
public final class Messenger {
    private Messenger(){}

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
        public void to(Queue queue) {
            this.queue = queue;
            send(this);
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
        QueueManager.add(receiver);
    }

    public static Receiver register(clear4j.msg.Receiver callback){
        return new Receiver(callback);
    }

    public static class Receiver implements clear4j.msg.queue.Receiver {
        private final clear4j.msg.Receiver callback;
        private Queue queue;

        private Receiver(clear4j.msg.Receiver callback){
            this.callback = callback;
        }

        @Override
        public void to(Queue queue) {
            this.queue = queue;
            register(this);
        }

        @Override
        public Queue getQueue() {
            return queue;
        }

        @Override
        public void onMessage(clear4j.msg.Message message) {
            callback.onMessage(message);
        }
    }


}
