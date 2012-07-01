package clear4j.msg;

import java.io.Serializable;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 17:10
 */
public interface QueueInfo<T extends Serializable> {
    Messenger.Receiver<T> to(String queue);
    String getQueue();
}
