package clear4j.msg.queue;

import java.io.Serializable;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message<T extends Serializable> extends RemoteOrigin {
    T getPayload();
}
