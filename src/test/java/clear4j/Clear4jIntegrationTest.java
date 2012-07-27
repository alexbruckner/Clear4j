package clear4j;

import clear4j.beans.Function;
import clear4j.config.Functions;
import clear4j.config.TestFunctions;
import clear4j.processor.Arg;

public class Clear4jIntegrationTest extends Clear4jTest {

    @Override
    protected Function[] getFunctions(){
        return new Function[]{ 
    		Functions.loadText(), Function.withArgs(TestFunctions.println(), new Arg<String>("test-key", "test-value"))
        };
    }

}
