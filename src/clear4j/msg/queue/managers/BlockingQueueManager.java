package clear4j.msg.queue.managers;

import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public final class BlockingQueueManager implements QueueManagement {

	private final Map<String, Queue> store;
	
	BlockingQueueManager() {
		this.store = new ConcurrentHashMap<String, Queue>();
	}

    @Override
    public void add(Message message) {
    	getQueue(message.getQueue()).getQueue().offer(message);
    }

    @Override
    public void add(Receiver receiver) {
    	getQueue(receiver.getQueue()).getReceivers().add(receiver);
    }

    @Override
    public void remove(Receiver receiver) {
    	getQueue(receiver.getQueue()).getReceivers().remove(receiver);
    }
    
    private Queue getQueue(String name){
    	Queue queue = store.get(name);
    	if (queue == null) {
    		queue = new Queue();
    		store.put(name, queue);
    	}
    	return queue;
    }


    private static final class Queue {

        private final BlockingQueue<Message> queue;
        private final List<Receiver> receivers;

        private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    	public Queue(){
    		this.queue = new LinkedBlockingQueue<Message>();
    		this.receivers = new CopyOnWriteArrayList<Receiver>();

            new Thread(){
                @Override
                public void run(){
                    while (true) {
                        try {
                            final Message message = queue.take();
                            for (final Receiver receiver : receivers){
                                notifyReceiver(receiver, message);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                private void notifyReceiver(final Receiver receiver, final Message message) {
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            receiver.onMessage(message);
                        }
                    });
                }
            }.start();

    	}


        public BlockingQueue<Message> getQueue(){
    		return queue;
    	}

		public List<Receiver> getReceivers() {
			return receivers;
		}
    }
}
