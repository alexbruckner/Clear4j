package clear4j.msg;

import java.io.Serializable;


/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:42
 */
public interface Message<T extends Serializable> extends QueueInfo<T>, RemoteInfo, Serializable {
    long getId();
    T getPayload();
	Message<T> toAndWait(String queue);
}
