package clear4j.msg.queue;

import java.io.Serializable;


public interface QueueManagement<T extends Serializable> {
    void add(Message<T> message);
    void add(Receiver<T> receiver);
    void remove(Receiver<T> receiver);
}
