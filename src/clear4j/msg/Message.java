package clear4j.msg;

import clear4j.msg.queue.Queue;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message {
    String getMessage();
    void to(Queue queue);
    Queue getQueue();
}
