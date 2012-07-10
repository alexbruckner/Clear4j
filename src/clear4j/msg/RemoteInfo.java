package clear4j.msg;

import clear4j.msg.queue.management.Adapter;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 17:10
 */
public interface RemoteInfo {
    Adapter on(String host, int port);
}
