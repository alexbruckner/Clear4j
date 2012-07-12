package clear4j.msg.beans;

import clear4j.msg.queue.Message;
import clear4j.msg.queue.Queue;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultMessage<T extends Serializable> extends AbstractRemoteOrigin implements Message<T> {
    private static final long serialVersionUID = 1L;
    private final T payload;

    private final static AtomicLong instanceCount = new AtomicLong();

    public DefaultMessage(Queue target, T payload) {
        super(target);
        this.payload = payload;
    }

    @Override
    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return String.format("%s->DefaultMessage{payload=%s}", super.toString(), payload);
    }
}
