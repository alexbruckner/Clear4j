package clear4j.msg.queue;

import java.io.Serializable;

import clear4j.msg.QueueInfo;
import clear4j.msg.RemoteInfo;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message<T extends Serializable> extends Serializable {
    long getId();
    T getPayload();
}
