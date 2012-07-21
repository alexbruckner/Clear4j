package clear4j.msg.beans;

import clear4j.msg.queue.Receiver;

import java.io.Serializable;

public class RemoteReceiver<T extends Serializable> extends DefaultReceiver<T> {

	private static final long serialVersionUID = 1L;
	
	private final Receiver<T> localProxy;
    private final String id;

    public RemoteReceiver(Receiver<T> receiver, Receiver<T> localProxy) {
        super(receiver.getTarget(), receiver.getMessageListener());
        this.localProxy = localProxy;
        this.id = receiver.getId();
    }

    public Receiver<T> getLocalProxy() {
        return localProxy;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("%s->RemoteReceiver{localProxy=%s}", super.toString(), localProxy);
    }
}
