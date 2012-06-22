package clear4j.msg;

import clear4j.msg.queue.Adapter;

import java.io.Serializable;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 17:10
 */
public interface RemoteInfo extends Serializable {
    Adapter on(String host, int port);
}
