package clear4j.msg;

import org.junit.Test;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class MonitorTest {


    @Test
    public void testMonitor() throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.start();
        Messenger.register("test", null);
        Thread.sleep(10000);
        monitor.stop();
    }

}
