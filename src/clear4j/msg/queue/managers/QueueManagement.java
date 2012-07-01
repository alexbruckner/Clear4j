package clear4j.msg.queue.managers;

import java.io.Serializable;

import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

public interface QueueManagement<T extends Serializable> {
    void add(Message<T> message);
    void add(Receiver<T> receiver);
    void remove(Receiver<T> receiver);
}
