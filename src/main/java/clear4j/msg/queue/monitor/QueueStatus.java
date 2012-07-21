package clear4j.msg.queue.monitor;

import clear4j.msg.queue.Receiver;

import java.util.List;

public interface QueueStatus extends Comparable<QueueStatus>{
    String getQueue();
    List<Receiver<?>> getReceivers();
}
