package clear4j.msg.queue.beans;

import clear4j.msg.queue.Host;

public final class HostPort implements Host {

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
    public boolean isLocal() {
        return this.equals(Host.LOCAL_HOST);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", host, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HostPort hostPort = (HostPort) o;

        return port == hostPort.port && host.equals(hostPort.host);

    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + port;
        return result;
    }
}
