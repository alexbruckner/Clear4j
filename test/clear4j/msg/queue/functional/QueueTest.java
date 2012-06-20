package clear4j.msg.queue.functional;

import clear4j.msg.queue.functional.Queue;
import junit.framework.Assert;
import org.junit.Test;

public class QueueTest {
	@Test
	public void testQueue(){
		Queue<Integer> queue = Queue.create("test");
		queue = Queue.push(queue, 1);
		queue = Queue.push(queue, 2);
		queue = Queue.push(queue, 3);

        StringBuilder sb = new StringBuilder();
        for (Integer i : queue){
            sb.append(i);
        }

        Assert.assertEquals("123", sb.toString());

	}
}
