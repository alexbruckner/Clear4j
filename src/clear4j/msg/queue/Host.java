package clear4j.msg.queue;

import clear4j.msg.queue.beans.HostPort;
import clear4j.msg.queue.utils.MessageUtils;

public interface Host {
    public static final Host LOCAL_HOST = new HostPort(MessageUtils.LOCAL_HOST, MessageUtils.LOCAL_PORT);
    String getHost();
    int getPort();
}
