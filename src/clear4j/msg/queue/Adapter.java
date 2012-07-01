package clear4j.msg.queue;

import java.io.Serializable;

import clear4j.msg.Messenger;

public interface Adapter<T extends Serializable> {
    Messenger.Receiver<T> to(String queue);
}
