package clear4j.msg;

import clear4j.msg.beans.DefaultMessage;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.beans.DefaultReceiver;
import clear4j.msg.queue.*;
import clear4j.msg.queue.beans.HostPort;
import clear4j.msg.queue.management.QueueManager;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:41
 */
public final class Messenger {
    private Messenger() {}

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    
    private static final Logger LOG = Logger.getLogger(Messenger.class.getName());


    /*
     * SENDING 
     */

    public static <T extends Serializable> void send(String queue, T payload) {
        send(new DefaultQueue(queue, Host.LOCAL_HOST), payload);
    }

    public static <T extends Serializable> void send(String host, int port, String queue, T payload) {
        send(new DefaultQueue(queue, new HostPort(host, port)), payload);
    }

    private static <T extends Serializable> void send(final Queue target, final T payload) {
		final Message<T> message = new DefaultMessage<T>(target, payload);

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("sending message: %s", message));
        }
        
        executor.execute(new Runnable(){
			@Override
			public void run() {
		        QueueManager.add(message);
			}
    	});
        
    }

    /*
     * RECEIVING
     */
//todo string queue should br Queue target
    public static <T extends Serializable> Receiver<T> register(final String queue, final MessageListener<T> listener) {    //todo remote

        Queue target = new DefaultQueue(queue, Host.LOCAL_HOST);
        final Receiver<T> receiver = new DefaultReceiver<T>(target, listener);

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("registering receiver: %s", receiver));
        }
        
        executor.execute(new Runnable(){
			@Override
			public void run() {
		        QueueManager.add(receiver);
			}
    	});
        
        return receiver;
    }

    public static <T extends Serializable> void unregister(final Receiver<T> receiver) {

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("un-registering receiver: %s", receiver));
        }
        
        executor.execute(new Runnable(){
			@Override
			public void run() {
		        QueueManager.remove(receiver);
			}
    	});
    }

    //TODO check this!!!
    public static synchronized void wait(String queue){ //todo remote
    	Queue target = new DefaultQueue(queue, Host.LOCAL_HOST);
    	Message<String> message = new DefaultMessage<String>(target, "wait");
    	try {
    		if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("waiting message received: %s", track(message).get()));
            }
		} catch (Exception e) {
			throw new RuntimeException(e); //TODO
		}
    }

    /*//TODO check this!!!
     * implicitly creates a Receiver to receive a message from a queue
     * initially used primarily for testing. better use Messenger.register(clear4j.msg.Receiver).to(queue);
     */
    public static <T extends Serializable> Future<Message<T>> track(final Message<T> original) {

        final CountDownLatch latch = new CountDownLatch(1);
        
        final Message<T>[] returned = new Message[1];

        final Receiver<T> receiver = register(original.getTarget().getName(), new MessageListener<T>() {
            @Override
            public void onMessage(clear4j.msg.queue.Message<T> message) {
                System.out.println("!!!!!!" + message + "==" + original.getId());
            	if(message.getId().equals(original.getId())){
                	returned[0] = message;
                	latch.countDown();
                }
            }
        });

        FutureTask<clear4j.msg.queue.Message<T>> futureTask = new FutureTask<Message<T>>(
            new Callable<Message<T>>() {
                @Override
                public Message<T> call() throws InterruptedException {
                    latch.await();
                    unregister(receiver);
                    return returned[0];
                }
            }
        );

        executor.execute(futureTask);

        return futureTask;
    }

 // TODO  the adapter stuff goes into the queue manager.
//    private static class ReceiverAdapter<T extends Serializable> implements clear4j.msg.queue.management.Adapter {
//
//    	private final Receiver<T> receiver;
//
//		public ReceiverAdapter(Receiver<T> receiver) {
//			this.receiver = receiver;
//		}
//
//		@Override
//		public Receiver<T> to(String queue) {
//			//create local proxy queue for this receiver.
//			String localProxyQueue = String.format("(%s/%s/%s)", receiver.host, receiver.port, queue);
//			receiver.to(localProxyQueue);
//			//send request to remote host, ie put a receiver message to its receivers queue
//			//this message will get picked up by the RemoteAdapter and a ('local' to the remote host) receiver created.
//			//which proxies all messages received back to the localProxyQueue.
//			String message = String.format("(%s/%s/%s)", LOCAL_HOST, LOCAL_PORT, localProxyQueue);
//			new Message<String>(message).on(receiver.host, receiver.port).to("remote-receivers");
//			return receiver;
//		}
//
//    }
//
//    private static class MessageAdapter<T extends Serializable> implements clear4j.msg.queue.management.Adapter {
//
//        private final Message<T> message;
//
//        private MessageAdapter(Message<T> message) {
//            this.message = message;
//        }
//
//        public Receiver<T> to(String queue) {
//            message.queue = queue;
//            try {
//                Socket socket = new Socket(message.host, message.port);
//                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
//                out.flush();
//                out.writeObject(message);
//                out.flush();
//                out.close();
//                socket.close();
//            } catch (IOException e) {
//                LOG.log(Level.SEVERE, e.getMessage());
//            }
//            return null; //TODO fix interface salad
//        }
//
//    }

}
