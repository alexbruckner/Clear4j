package clear4j.msg.queue.beans;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.utils.MessageUtils;

public class LocalHost implements Host {

    private final String host;
    private final int port;

    public LocalHost() {
        this.host = MessageUtils.LOCAL_HOST;
        this.port = MessageUtils.LOCAL_PORT;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", host, port);
    }
}
