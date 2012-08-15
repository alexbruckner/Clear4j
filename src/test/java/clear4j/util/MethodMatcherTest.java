package clear4j.util;

import clear4j.config.Functions;
import clear4j.processor.Param;
import org.junit.Test;

public class MethodMatcherTest {


    @Test
    public void testMethodMatching(){     //TODO assert statements!


        ReflectionUtils.match(Functions.printNewLineOnly());
        ReflectionUtils.match(Functions.println());
        ReflectionUtils.match(Functions.println(new Param<String>("key1", "value1"), new Param<String>("key2", "value2")));


    }

}
