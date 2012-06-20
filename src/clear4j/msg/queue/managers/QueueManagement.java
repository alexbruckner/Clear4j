package clear4j.msg.queue.managers;

import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

public interface QueueManagement {
    void add(Message message);
    void add(Receiver receiver);
    void remove(Receiver receiver);
    void start();
}
