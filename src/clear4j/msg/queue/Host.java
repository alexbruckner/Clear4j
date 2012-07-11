package clear4j.msg.queue;

import clear4j.msg.queue.beans.LocalHost;

public interface Host {
    public static final Host LOCAL_HOST = new LocalHost();
    String getHost();
    int getPort();
}
