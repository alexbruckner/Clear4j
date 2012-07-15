package clear4j.msg.queue.monitor;

import clear4j.msg.queue.Receiver;

import java.io.Serializable;
import java.util.List;

public interface QueueStatus<T extends Serializable> extends Comparable<QueueStatus<T>>{
    String getQueue();
    List<Receiver<T>> getReceivers();
}