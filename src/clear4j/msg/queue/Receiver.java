package clear4j.msg.queue;

import java.io.Serializable;



/**
 * User: alex
 * Date: 27/05/12
 * Time: 16:15
 */
public interface Receiver<T extends Serializable> {
    MessageListener<T> getMessageListener();
    Queue getQueue();
}
