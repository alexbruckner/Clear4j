package clear4j.msg;

import java.io.Serializable;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message extends QueueInfo, RemoteInfo, Serializable {
    String getMessage();
    long getId();
    String getValue();
}
