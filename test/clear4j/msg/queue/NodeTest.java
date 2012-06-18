package clear4j.msg.queue;

import org.junit.Assert;
import org.junit.Test;

public class NodeTest {

	@Test
	public void testNodes(){
		
		Node<Integer> first = Node.create(1);
		Node<Integer> current = first;
		
		for (int i = 2; i < 6; i++){
			current = Node.append(current, i);
		}
		
		// format: Node[(id)(previous.id, payload, next.id)]
		// previous node written below current one until first node.
		String expected = "Node[(5)(4,5)]\n"
						+ "Node[(4)(3,4)]\n"
						+ "Node[(3)(2,3)]\n"
						+ "Node[(2)(1,2)]\n"
						+ "Node[(1)(-1,1)]\n";
		
		Assert.assertEquals(expected, current.toString());
		
		StringBuilder sb = new StringBuilder();
        for (Integer i : current){
            sb.append(i);
        }

        Assert.assertEquals("54321", sb.toString());
	}
	
}
