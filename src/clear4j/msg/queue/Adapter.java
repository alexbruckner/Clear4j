package clear4j.msg.queue;

import clear4j.msg.Messenger;

public interface Adapter {
    Messenger.Receiver to(String queue);
}
