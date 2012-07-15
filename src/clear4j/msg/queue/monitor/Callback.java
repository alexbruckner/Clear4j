package clear4j.msg.queue.monitor;

import java.util.Set;

public interface Callback {
    void call(Set<QueueStatus> status);
}
