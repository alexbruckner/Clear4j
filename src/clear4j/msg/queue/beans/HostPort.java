package clear4j.msg.queue.beans;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.utils.MessageUtils;

public class HostPort implements Host {

    private final String host;
    private final int port;

    public HostPort(final String host, final int port) {
        this.host = host;
        this.port = port;
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
