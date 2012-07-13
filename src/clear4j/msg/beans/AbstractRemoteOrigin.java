package clear4j.msg.beans;

import clear4j.msg.queue.Host;
import clear4j.msg.queue.Queue;
import clear4j.msg.queue.RemoteOrigin;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractRemoteOrigin implements RemoteOrigin {
    private static final long serialVersionUID = 1L;
    private final Queue target;
    private final Host origin;
    private final String id;

    private static final AtomicLong instanceCount = new AtomicLong();

    public AbstractRemoteOrigin(final Queue target) {
        this.target = target;
        this.origin = Host.LOCAL_HOST;
        this.id = String.format("%s-%s-%s", origin, System.currentTimeMillis(), instanceCount.addAndGet(1));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Host getOrigin() {
        return origin;
    }

    @Override
    public Queue getTarget() {
        return target;
    }

    @Override
    public boolean isLocal() {
        return target.getHost().isLocal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractRemoteOrigin)) return false;

        AbstractRemoteOrigin that = (AbstractRemoteOrigin) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("AbstractRemoteOrigin{target=%s, origin=%s, id='%s'}", target, origin, id);
    }
}
