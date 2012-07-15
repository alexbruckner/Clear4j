package clear4j.msg.queue.beans.monitor;

import clear4j.msg.queue.Receiver;
import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class DefaultQueueStatus<T extends Serializable> implements QueueStatus<T> {

    private final String queue;
    private final List<Receiver<T>> receivers;

    public DefaultQueueStatus(final String queue, final List<Receiver<T>> receivers) {
        this.queue = queue;
        this.receivers = Collections.unmodifiableList(receivers);
    }

    @Override
    public String getQueue() {
        return queue;
    }

    @Override
    public List<Receiver<T>> getReceivers() {
        return receivers;
    }

    @Override
    public String toString() {
        return String.format("DefaultQueueStatus{queue='%s', receivers=%s}", queue, receivers);
    }
}
