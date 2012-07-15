package clear4j.msg;

import clear4j.msg.beans.ExtendedQueueStatus;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.msg.queue.management.QueueManager;
import clear4j.msg.queue.monitor.Callback;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Monitor implements Runnable {
    private final int frequency; //milliseconds
    private boolean running = true;
    private Thread monitorThread;
    private Callback<ExtendedQueueStatus> callback;
    private final Map<String, MessageCounter> counters = new ConcurrentHashMap<String, MessageCounter>();
    

    public Monitor(final Callback callback) {
        this(5000, callback);
    }

    public Monitor(final int frequency, final Callback callback) {
        this.frequency = 5000;
        this.callback = callback;
    }

    /*
    * Monitor method
    */

    private void monitor(){
        Set<QueueStatus> status = QueueManager.status();
        //now decorate the queue status with message count, etc... from local receivers
        Set<ExtendedQueueStatus> msgStatus = new TreeSet<ExtendedQueueStatus>();
        for (QueueStatus queueStatus : status){
        	int msgCount = getMessageCount(queueStatus.getQueue());
        	msgStatus.add(new ExtendedQueueStatus(queueStatus, msgCount));
        }
        callback.call(msgStatus);
    }

    private int getMessageCount(String queue) {
    	if (!counters.containsKey(queue)){
    		counters.put(queue, new MessageCounter(queue));
    		return 0;
    	}
    	return counters.get(queue).getCount();
	}

	/*
     * Thread methods
     */

    @Override
    public void run() {
        try {
            while (running) {
                monitor();
                Thread.sleep(frequency);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        if (running) {
            monitorThread = new Thread(this);
            monitorThread.start();
        }
    }

    public void stop() {
        if (running) {
            running = false;
            monitorThread.interrupt();
        }
    }
    
    private static final class MessageCounter<T extends Serializable> implements MessageListener<T> {
    	private final AtomicInteger COUNT = new AtomicInteger();
		private final String queue;
    	
		MessageCounter(final String queue){
    		this.queue = queue;
    		Messenger.register(queue, this);
    	}
    	
    	@Override
		public void onMessage(Message<T> message) {
			COUNT.addAndGet(1);
		}
		
		int getCount(){
			return COUNT.intValue();
		}
		
		@Override
		public boolean equals (Object o){
			MessageCounter<T> other = (MessageCounter<T>) o;
			return queue.equals(other.queue);
		}
		
		@Override
		public int hashCode() {
			return queue.hashCode();
		}
    }

}
