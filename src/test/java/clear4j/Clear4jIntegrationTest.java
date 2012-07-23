package clear4j;

import clear4j.config.TestFunctions;
import clear4j.processors.FileUtils;
import junit.framework.Assert;
import org.junit.Test;

public class Clear4jIntegrationTest extends Clear4jTest {

    @Test
    public void testRemoteWorkFlow() throws Exception {

        // start the workflow process
        String filePath = TestConfig.TEST_FILE_PATH.getValue();
        Workflow workflow = new Workflow(filePath, Functions.loadText(), TestFunctions.println());
        Clear.run(workflow);

        // wait for the result
        String text1 = workflow.waitFor();

        // load it the boring way
        String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        // assert same content
        Assert.assertEquals(text2, text1);

    }
}
