package clear4j.util;

import clear4j.config.TestFunctions;
import clear4j.processor.instruction.Instruction;
import junit.framework.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {

	@Test
	public void testMethodMatching() {     //TODO assert statements!
		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.printNewLineOnly()));
		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println()));
	}

	@Test
	public void testMethodInvoking() {     //TODO assert statements!
		Assert.assertNull(ReflectionUtils.invoke(new Instruction<String>(TestFunctions.printNewLineOnly())));
		Assert.assertEquals("test", ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(), "test")));
	}

}

