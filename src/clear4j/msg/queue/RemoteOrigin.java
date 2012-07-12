package clear4j.msg.queue;

import java.io.Serializable;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface RemoteOrigin extends Serializable {
    Host getOrigin();
    Queue getTarget();
    String getId(); //should consist of origin.host-origin.port-timestamp-counter (counter=original long id)
}
