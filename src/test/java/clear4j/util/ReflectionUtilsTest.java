package clear4j.util;

import clear4j.config.TestFunctions;
import clear4j.processor.Param;
import clear4j.processor.instruction.Instruction;
import junit.framework.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void testMethodMatching(){     //TODO assert statements!
		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.printNewLineOnly()));
		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println()));
		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println(new Param<String>("key1", "value1"), new Param<String>("key2", "value2"))));
//		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println(new Param<String>("key2", "value2"), new Param<String>("key1", "value1"))));
//		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println(new Param<String>("key1", "value1"))));
//		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println(new Param<String>("key2", "value2"))));
	}

	@Test
	public void testMethodInvoking(){     //TODO assert statements!
		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.printNewLineOnly(), null)); //TODO remove need for null
		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(), "test"));
		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(new Param<String>("key1", "value1"), new Param<String>("key2", "value2")), "test"));
//		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(new Param<String>("key2", "value2"), new Param<String>("key1", "value1")), "test"));
//		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(new Param<String>("key1", "value1")), "test"));
//		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(new Param<String>("key2", "value2")), "test"));
	}

}

