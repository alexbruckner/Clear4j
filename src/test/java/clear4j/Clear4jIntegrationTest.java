package clear4j;

import clear4j.config.TestFunctions;

public class Clear4jIntegrationTest extends Clear4jTest {

	protected Function[] functions = new Function[]{
    		Functions.loadText(), TestFunctions.println()
    };
    
}
