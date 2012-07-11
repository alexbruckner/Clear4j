package clear4j.msg.beans;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.Queue;

public class DefaultQueue implements Queue {

    private final String name;
    private final Host host;

    public DefaultQueue(String name, Host host) {
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
}
