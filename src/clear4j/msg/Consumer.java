package clear4j.msg;

/**
 * User: alex
 * Date: 27/05/12
 * Time: 16:15
 */
public interface Consumer {
    void onMessage(Message message);
}
