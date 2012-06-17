package clear4j.msg.queue;

import org.junit.Test;

public class QueueTest {
	@Test
	public void testQueue(){
		Queue<Integer> queue = Queue.create("test");
		queue = Queue.push(queue, 1);
		queue = Queue.push(queue, 2);
		System.out.println(queue);
	}
}
