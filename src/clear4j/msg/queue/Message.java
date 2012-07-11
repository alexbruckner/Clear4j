package clear4j.msg.queue;

import java.io.Serializable;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message<T extends Serializable> extends Serializable {
    String getId(); //should consist of origin.host-origin.port-timestamp-counter (counter=original long id)
    Queue getTarget();
    Host getOrigin();
    T getPayload();
}
