package clear4j.msg.beans;

import clear4j.msg.queue.MessageListener;
import clear4j.msg.queue.Queue;
import clear4j.msg.queue.Receiver;

import java.io.Serializable;

public class DefaultReceiver<T extends Serializable> implements Receiver<T> {

    private final Queue queue;
    private final MessageListener<T> messageListener;

    public DefaultReceiver(Queue queue, MessageListener<T> messageListener) {
        this.queue = queue;
        this.messageListener = messageListener;
    }

    @Override
    public MessageListener<T> getMessageListener() {
        return messageListener;
    }

    @Override
    public Queue getQueue() {
        return queue;
    }
}
