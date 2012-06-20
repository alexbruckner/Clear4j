package clear4j.msg.queue.managers;

import clear4j.msg.Message;
import clear4j.msg.queue.Receiver;

public class FunctionalQueueManager implements QueueManagement {

    @Override
    public void add(Message message) {
        throw new UnsupportedOperationException("to be implemented...");
    }

    @Override
    public void add(Receiver receiver) {
        throw new UnsupportedOperationException("to be implemented...");
    }

    @Override
    public void remove(Receiver receiver) {
        throw new UnsupportedOperationException("to be implemented...");
    }

}
