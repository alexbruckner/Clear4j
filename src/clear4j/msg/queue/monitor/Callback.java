package clear4j.msg.queue.monitor;

import java.util.Set;

public interface Callback<T extends QueueStatus<?>> {
    void call(Set<T> status);
}
