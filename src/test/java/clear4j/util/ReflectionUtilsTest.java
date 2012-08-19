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
		Assert.assertNotNull(ReflectionUtils.match(TestFunctions.println(new Param<String>("value1"), new Param<String>("value2"))));
	}

	@Test
	public void testMethodInvoking(){     //TODO assert statements!
		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.printNewLineOnly(), null)); //TODO remove need for null
		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(), "test"));
		ReflectionUtils.invoke(new Instruction<String>(TestFunctions.println(new Param<String>("value1"), new Param<String>("value2")), "test"));
	}

}

