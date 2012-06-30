package clear4j.msg;

import clear4j.msg.queue.Adapter;
import clear4j.msg.queue.managers.QueueManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
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
    
    private static final int DEFAULT_PORT = 9876;
    
    private static final String LOCAL_HOST = getLocalHost();
    
    private static final int LOCAL_PORT = getLocalPort();
    
    private static String getLocalHost(){
    	try {
			return java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			LOG.log(Level.SEVERE, e.getMessage());
		}
    	return null;
    }
    
    private static int getLocalPort(){
    	String property = System.getProperty("clear4j.port");
    	if (property != null){
    		try{
    			return Integer.parseInt(property);
    		} catch (NumberFormatException e){
    			LOG.log(Level.WARNING, e.getMessage());
    			return DEFAULT_PORT;
    		}
    	} else {
    		return DEFAULT_PORT;
    	}
    }
    
    /*
     * SENDING //TODO clean up multiple declarations essentially all doing the same
     * 
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
    
    public static Message send(String key, Serializable value){
    	return new Message(key, value);
    }

    public static void waitFor(String name){
        waitFor(name, String.format("<waitFor queue=\"%s\"/>", name));
    }

    public static void waitFor(String name, String message) {
        // register temporary receiver and send a message. once we get it back,
        // we know that all previous messages should have been dealt with.
        final Message waitForMessage = new Message(message);

        final boolean[] received = {false};

        //register
        Receiver receiver = register(new clear4j.msg.Receiver() {
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
        unregister(receiver);
    }

    private static class Message implements clear4j.msg.Message {
		private static final long serialVersionUID = 1L;
		private final String message;
		private final Serializable payload;
        private String queue;
        private String host;
        private int port;

        private final long id;
        private final static AtomicLong count = new AtomicLong();

        private Message(String message) {
            this.message = message;
            this.id = count.addAndGet(1);
            this.payload = null;
        }

		private Message(String key, Serializable value) {
			this.message = key;
			this.id = count.addAndGet(1);
			this.payload = value;
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
            return null; //TODO fix interface salad or allow to specify synchronous receiver of one message with FutureTask.
        }

        @Override
        public Adapter on(String host, int port) {
            this.host = host;
            this.port = port;
            return new MessageAdapter(this);
        }

        @Override
        public String getQueue() {
            return queue;
        }

        public long getId() { //TODO add host/port
            return id;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "message='" + message + '\'' +
                    ", queue=" + queue +
                    '}';
        }

		@Override
		public Serializable getPayload() {
			return payload;
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
        private String host;
        private int port;

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

		@Override
        public Adapter on(String host, int port) {
            this.host = host;
            this.port = port;
            return new ReceiverAdapter(this);
        }
    }
    
    private static class ReceiverAdapter implements clear4j.msg.queue.Adapter {

    	private final Receiver receiver;
    	
		public ReceiverAdapter(Receiver receiver) {
			this.receiver = receiver;
		}

		@Override
		public Receiver to(String queue) {
			//create local proxy queue for this receiver.
			String localProxyQueue = String.format("(%s/%s/%s)", receiver.host, receiver.port, queue);
			receiver.to(localProxyQueue);
			//send request to remote host, ie put a receiver message to its receivers queue
			//this message will get picked up by the RemoteAdapter and a ('local' to the remote host) receiver created.
			//which proxies all messages received back to the localProxyQueue.
			String message = String.format("(%s/%s/%s)", LOCAL_HOST, LOCAL_PORT, localProxyQueue);
			newMessage(message).on(receiver.host, receiver.port).to("remote-receivers");
			return receiver;
		}

    }

    private static class MessageAdapter implements clear4j.msg.queue.Adapter {

        private final Message message;

        private MessageAdapter(Message message) {
            this.message = message;
        }

        public Receiver to(String queue) {
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
            return null; //TODO fix interface salad
        }

    }

}
