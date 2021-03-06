package clear4j.msg.queue.beans.monitor;

import clear4j.msg.queue.Receiver;
import clear4j.msg.queue.monitor.QueueStatus;

import java.util.Collections;
import java.util.List;

public class DefaultQueueStatus implements QueueStatus {

    private final String queue;
    private final List<Receiver<?>> receivers;

    public DefaultQueueStatus(final String queue, final List<Receiver<?>> receivers) {
        this.queue = queue;
        this.receivers = Collections.unmodifiableList(receivers);
    }

    @Override
    public String getQueue() {
        return queue;
    }

    @Override
    public List<Receiver<?>> getReceivers() {
        return receivers;
    }

    @Override
    public String toString() {
        return String.format("DefaultQueueStatus{queue='%s', receivers=%s}", queue, receivers);
    }

    @Override
    public int compareTo(QueueStatus o) {
        return this.getQueue().compareTo(o.getQueue());
    }
}
