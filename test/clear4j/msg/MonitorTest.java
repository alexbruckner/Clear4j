package clear4j.msg;

import clear4j.msg.beans.monitor.MonitorFrame;
import org.junit.Test;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class MonitorTest {


    @Test
    public void testMonitor() throws InterruptedException {
        new MonitorFrame();
        Thread.sleep(12000);
    }

}
