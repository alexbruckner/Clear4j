package clear4j.msg.beans;

import clear4j.msg.queue.Message;
import clear4j.msg.queue.Queue;

import java.io.Serializable;

public class DefaultMessage<T extends Serializable> extends AbstractRemoteOrigin implements Message<T> {
    private static final long serialVersionUID = 1L;
    private final T payload;

    public DefaultMessage(final Queue target, final T payload) {
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
