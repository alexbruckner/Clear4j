package clear4j;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.config.Functions;
import clear4j.config.Workflows;
import clear4j.processor.Arg;
import clear4j.processors.FileUtils;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class Clear4jTest {

    @BeforeClass
    public static void setup(){
        FileUtils.writeTextToFile(TestConfig.TEST_FILE_PATH.getValue(), TestConfig.TEST_FILE_CONTENT.getValue());
    }

    @AfterClass
    public static void tearDown(){
        FileUtils.removeFile(TestConfig.TEST_FILE_PATH.getValue());
    }

    protected Function[] getFunctions(){
        return new Function[]{ 
    		Functions.loadText(), Functions.println(new Arg<String>("test-key", "test-value"))
        };
    }

    @Test
    public void testMonitorFunction(){
        Clear.run(Workflows.getMonitorWorkflow()).waitFor();
    }
    
    @Test
    public void testCheckedException() {
    	Clear.run(new Workflow(Functions.throwCheckedException())).waitFor();
    }
    
    @Test
    public void testRuntimeException() {
    	Clear.run(new Workflow(Functions.throwRuntimeException())).waitFor();
    }
    
    @Test
    public void testPrintlnNull() {
    	Assert.assertNull(Clear.run(new Workflow(Functions.println())).waitFor());
    }

    @Test
    public void testSimpleWorkFlow() throws Exception {
        // start the workflow process
        String filePath = TestConfig.TEST_FILE_PATH.getValue();
        Workflow workflow = new Workflow(filePath, getFunctions());
        Clear.run(workflow);

        // wait for the result
        String text1 = workflow.waitFor();
		Assert.assertNotNull(text1);

		// load it the boring way
		String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());
		Assert.assertNotNull(text2);

		// assert same content
        Assert.assertEquals(text2, text1);
    	
    }

	@Test
	public void testPrintProcessor() {

		//println without initial value (ie calls void println() method)
		Clear.run(new Workflow(Functions.println())).waitFor();

		//println without initial value (ie calls Object println(Object value) method) //TODO remove need to set function array
		Clear.run(new Workflow("piped value test", new Function[]{Functions.println()})).waitFor();

		//println without initial value (ie calls Object println(Object valuwe, Arg[] args) method)
		Clear.run(new Workflow("piped value test", new Function[]{Functions.println(new Arg<String>("key1", "value1"), new Arg<String>("key2", "value2"))})).waitFor();




	}
  
}
