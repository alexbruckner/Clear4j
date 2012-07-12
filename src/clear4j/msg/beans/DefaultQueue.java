package clear4j.msg.beans;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.Queue;

public final class DefaultQueue implements Queue {

    private final String name;
    private final Host host;

    public DefaultQueue(final String name, final Host host) {
        this.name = name;
        this.host = host;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Host getHost() {
        return host;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultQueue that = (DefaultQueue) o;

        return host.equals(that.host) && name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + host.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("DefaultQueue{name='%s', host=%s}", name, host);
    }
}
