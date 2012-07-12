package clear4j.msg.beans;

import clear4j.msg.queue.MessageListener;
import clear4j.msg.queue.Queue;
import clear4j.msg.queue.Receiver;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultReceiver<T extends Serializable> extends AbstractRemoteOrigin implements Receiver<T> {
    private final MessageListener<T> messageListener;

    private final static AtomicLong instanceCount = new AtomicLong();

    public DefaultReceiver(final Queue target, final MessageListener<T> messageListener) {
        super(target);
        this.messageListener = messageListener;
    }

    @Override
    public MessageListener<T> getMessageListener() {
        return messageListener;
    }

    @Override
    public String toString() {
        return String.format("%s->DefaultReceiver{messageListener=%s}", super.toString(), messageListener);
    }
}
