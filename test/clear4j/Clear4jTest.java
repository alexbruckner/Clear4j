package clear4j;

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

    @Test
    public void testScenario() {

        // under the bonnet: we send an instruction message "load" to the file processor
        // with payload Payload.FILE_PATH = "/tmp/test.txt"
        // instruct(Enum 'The' -> selection: FileProcessor) returns a processor.FileProcessor
        // with certain methods that create instruction messages to be put on the file processor queue.
        // this queue can be local to the jvm or distributed.
        Workflow workflow = Clear.instruct(The.FileProcessor).toLoad(TestConfig.TEST_FILE_PATH.getValue());
        Payload payload = workflow.waitFor();
        String text1 = payload.get(Payload.TEXT);

        // load it the boring way
        String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        Assert.assertEquals(text2, text1);

    }



}
