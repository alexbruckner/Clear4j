package clear4j.msg;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.Queue;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultMessage<T extends Serializable> implements Message<T> {
    private static final long serialVersionUID = 1L;
    private final Queue target;
    private final Host origin;
    private final T payload;
    private final String id;

    private final static AtomicLong instanceCount = new AtomicLong();

    private DefaultMessage(Queue target, T payload) {
        this.target = target;
        this.payload = payload;
        this.origin = Host.LOCAL_HOST;
        this.id = String.format("%s-%s-%s", origin, System.currentTimeMillis(), instanceCount.addAndGet(1));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Host getOrigin() {
        return origin;
    }

    @Override
    public Queue getTarget() {
        return target;
    }

    @Override
    public T getPayload() {
        return payload;
    }
}
