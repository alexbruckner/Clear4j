package clear4j.msg.beans;

import clear4j.msg.queue.MessageListener;
import clear4j.msg.queue.Receiver;

import java.io.Serializable;

public class DefaultReceiver<T extends Serializable> implements Receiver<T> {

    private final MessageListener messageListener;
    private final String queue;

    public DefaultReceiver(MessageListener messageListener, String queue) {
        this.messageListener = messageListener;
        this.queue = queue;
    }

    @Override
    public MessageListener<T> getMessageListener() {
        return messageListener;
    }

    @Override
    public String getQueue() {
        return queue;
    }
}
