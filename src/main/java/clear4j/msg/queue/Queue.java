package clear4j.msg.queue;

import java.io.Serializable;

public interface Queue extends Serializable {
    String getName();
    Host getHost();
}
