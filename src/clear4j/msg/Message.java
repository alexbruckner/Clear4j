package clear4j.msg;

import java.io.Serializable;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message<T extends Serializable> extends QueueInfo, RemoteInfo, Serializable {
    long getId();
    T getPayload();
}
