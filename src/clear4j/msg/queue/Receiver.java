package clear4j.msg.queue;

import java.io.Serializable;

import clear4j.msg.QueueInfo;
import clear4j.msg.RemoteInfo;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 17:11
 */
public interface Receiver<T extends Serializable> extends clear4j.msg.Receiver<T>, QueueInfo<T>, RemoteInfo {
}
