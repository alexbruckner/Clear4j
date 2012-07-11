package clear4j.msg;

import clear4j.msg.queue.management.QueueManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.*;
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
    
   
    public static <T extends Serializable> clear4j.msg.queue.Message<T> send(T payload){
    	return new Message<T>(payload);
    }

    public static void waitFor(String name){
        waitFor(name, String.format("<waitFor queue=\"%s\"/>", name));
    }

    public static <T extends Serializable> void waitFor(String name, T message) {
        // register temporary receiver and send a message. once we get it back,
        // we know that all previous messages should have been dealt with.
        final Message<T> waitForMessage = new Message<T>(message);

        final boolean[] received = {false};

        //register
        Receiver<T> receiver = register(new clear4j.msg.queue.Receiver<T>() {
            @Override
            public void onMessage(clear4j.msg.queue.Message<T> message) {
                if (message.getId().equals(waitForMessage.getId())) {
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


    /*
     * RECEIVING
     */

    private static <T extends Serializable> void register(Receiver<T> receiver) {
        if (receiver.getQueue() == null) {
            throw new RuntimeException("Receiver needs a queue");
        }
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("registering receiver: %s", receiver));
        }
        QueueManager.add(receiver);
    }

    public static <T extends Serializable> Receiver<T> register(clear4j.msg.queue.Receiver<T> callback) {
        return new Receiver<T>(callback);
    }

    /*
     * implicitly creates a Receiver to receive a message from a queue
     * initially used primarily for testing. better use Messenger.register(clear4j.msg.Receiver).to(queue);
     */
    public static <T extends Serializable> Future<clear4j.msg.queue.Message<T>> track(final long messageId, final String queue) {

        final CountDownLatch latch = new CountDownLatch(1);
        final clear4j.msg.queue.Message<T>[] returned = new clear4j.msg.queue.Message[1];

        final Receiver<T> receiver = register(new clear4j.msg.queue.Receiver<T>() {
            @Override
            public void onMessage(clear4j.msg.queue.Message<T> message) {
                if(message.getId() == messageId){
                	returned[0] = message;
                	latch.countDown();
                }
            }
        }).to(queue);

        FutureTask<clear4j.msg.queue.Message<T>> futureTask = new FutureTask<clear4j.msg.queue.Message<T>>(
            new Callable<clear4j.msg.queue.Message<T>>() {
                @Override
                public clear4j.msg.queue.Message<T> call() throws InterruptedException {
                    latch.await();
                    unregister(receiver);
                    return returned[0];
                }
            }
        );

        executor.execute(futureTask);

        return futureTask;
    }

    public static <T extends Serializable> void unregister(Receiver<T> receiver) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("un-registering receiver: %s", receiver));
        }
        QueueManager.remove(receiver);
    }


    private static class ReceiverAdapter<T extends Serializable> implements clear4j.msg.queue.management.Adapter {

    	private final Receiver<T> receiver;
    	
		public ReceiverAdapter(Receiver<T> receiver) {
			this.receiver = receiver;
		}

		@Override
		public Receiver<T> to(String queue) {
			//create local proxy queue for this receiver.
			String localProxyQueue = String.format("(%s/%s/%s)", receiver.host, receiver.port, queue);
			receiver.to(localProxyQueue);
			//send request to remote host, ie put a receiver message to its receivers queue
			//this message will get picked up by the RemoteAdapter and a ('local' to the remote host) receiver created.
			//which proxies all messages received back to the localProxyQueue.
			String message = String.format("(%s/%s/%s)", LOCAL_HOST, LOCAL_PORT, localProxyQueue);
			new Message<String>(message).on(receiver.host, receiver.port).to("remote-receivers");
			return receiver;
		}

    }

    private static class MessageAdapter<T extends Serializable> implements clear4j.msg.queue.management.Adapter {

        private final Message<T> message;

        private MessageAdapter(Message<T> message) {
            this.message = message;
        }

        public Receiver<T> to(String queue) {
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
