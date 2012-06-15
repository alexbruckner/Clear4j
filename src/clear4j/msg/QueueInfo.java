package clear4j.msg;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 17:10
 */
public interface QueueInfo {
    Messenger.Receiver to(String queue);
    String getQueue();
}
