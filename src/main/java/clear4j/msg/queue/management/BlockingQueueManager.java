package clear4j.msg.queue.management;

import clear4j.msg.queue.Message;
import clear4j.msg.queue.QueueManagement;
import clear4j.msg.queue.Receiver;
import clear4j.msg.queue.beans.monitor.DefaultQueueStatus;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;

public final class BlockingQueueManager<T extends Serializable> implements QueueManagement<T> {

	private final Map<String, Queue<T>> store;
	
	BlockingQueueManager() {
		this.store = new ConcurrentHashMap<String, Queue<T>>();
	}

    @Override
    public void add(Message<T> message) {
    	getQueue(message.getTarget().getName()).getQueue().offer(message);
    }

    @Override
    public void add(Receiver<T> receiver) {
    	getQueue(receiver.getTarget().getName()).getReceivers().add(receiver);
    }

    @Override
    public void remove(Receiver<T> receiver) {
    	getQueue(receiver.getTarget().getName()).getReceivers().remove(receiver);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Set<QueueStatus> status() {
        Set<QueueStatus> status = new TreeSet<QueueStatus>();
        for (Map.Entry<String, Queue<T>> entry : store.entrySet()){
            String name = entry.getKey();
            Queue<T> queue = entry.getValue();
            List receivers = queue.getReceivers();
            status.add(new DefaultQueueStatus(name, receivers));
        }
        return status;
    }

    private Queue<T> getQueue(String name){
    	Queue<T> queue = store.get(name);
    	if (queue == null) {
    		queue = new Queue<T>();
    		store.put(name, queue);
    	}
    	return queue;
    }


    private static final class Queue<T extends Serializable> {

        private final BlockingQueue<Message<T>> queue;
        private final List<Receiver<T>> receivers;

        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    	public Queue(){
    		this.queue = new LinkedBlockingQueue<Message<T>>();
    		this.receivers = new CopyOnWriteArrayList<Receiver<T>>();

            new Thread(){
                @Override
                public void run(){
                    while (true) {
                        try {
                            final Message<T> message = queue.take();
                            for (final Receiver<T> receiver : receivers){
                                notifyMessageListener(receiver, message);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                private void notifyMessageListener(final Receiver<T> receiver, final Message<T> message) {
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                        	try {
                        		receiver.getMessageListener().onMessage(message);
                        	} catch (Throwable e) {
                        		e.printStackTrace(System.err);
                        	}
                        }
                    });
                }
            }.start();

    	}


        public BlockingQueue<Message<T>> getQueue(){
    		return queue;
    	}

		public List<Receiver<T>> getReceivers() {
			return receivers;
		}
    }
}
