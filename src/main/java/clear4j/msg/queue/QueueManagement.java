package clear4j.msg.queue;

import clear4j.msg.queue.monitor.QueueStatus;

import java.io.Serializable;
import java.util.Set;


public interface QueueManagement<T extends Serializable> {
    void add(Message<T> message);
    void add(Receiver<T> receiver);
    void remove(Receiver<T> receiver);
    Set<QueueStatus> status();
}
