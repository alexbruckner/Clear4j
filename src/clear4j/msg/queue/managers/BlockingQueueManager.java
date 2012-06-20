package clear4j.msg.queue.managers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

public class BlockingQueueManager implements QueueManagement {

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
    	
    	public Queue(){
    		this.queue = new LinkedBlockingQueue<Message>();
    		this.receivers = new CopyOnWriteArrayList<Receiver>();
    		
    		//TODO start thread per queue and block
    		
    	}
    	
    	public BlockingQueue<Message> getQueue(){
    		return queue;
    	}

		public List<Receiver> getReceivers() {
			return receivers;
		}
    }
}
