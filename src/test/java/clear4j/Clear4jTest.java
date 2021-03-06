package clear4j;

import clear4j.beans.Workflow;
import clear4j.config.TestFunctions;
import clear4j.config.Workflows;
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
	public static void setup() {
		System.setProperty("clear4j.config.class", "clear4j.config.TestFunctions");
		FileUtils.writeTextToFile(TestConfig.TEST_FILE_PATH.getValue(), TestConfig.TEST_FILE_CONTENT.getValue());
	}

	@AfterClass
	public static void tearDown() {
		FileUtils.removeFile(TestConfig.TEST_FILE_PATH.getValue());
	}

	protected Workflow getWorkflow(String path) {
		return new Workflow(path, TestFunctions.loadText(), TestFunctions.println());
	}


	@Test
	public void testMonitorFunction() {
		Clear.run(Workflows.getMonitorWorkflow()).waitFor();
	}

	@Test
	public void testCheckedException() {
		Clear.run(new Workflow(TestFunctions.throwCheckedException())).waitFor();
	}

	@Test
	public void testRuntimeException() {
		Clear.run(new Workflow(TestFunctions.throwRuntimeException())).waitFor();
	}

	@Test
	public void testPrintlnNull() {
		Assert.assertNull(Clear.run(new Workflow(TestFunctions.println())).waitFor());
	}

	@Test
	public void testSimpleWorkFlow() throws Exception {
		// start the workflow process
		String filePath = TestConfig.TEST_FILE_PATH.getValue();
		Workflow workflow = getWorkflow(filePath);
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
	public void testPrintProcessor() {    //TODO add assert statements

		//println without initial value (ie calls void println() method)
		Clear.run(new Workflow(TestFunctions.println())).waitFor();

		//println without initial value (ie calls Object println(Object value) method) //TODO remove need to set function array
		Clear.run(new Workflow("piped value test", TestFunctions.println())).waitFor();

	}

}
