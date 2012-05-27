package clear4j.msg;

import clear4j.msg.queue.Queue;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 17:10
 */
public interface QueueInfo {
    void to(Queue queue);
    Queue getQueue();
}
