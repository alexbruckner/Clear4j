package clear4j;

import clear4j.processors.FileUtils;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class Clear4jTest {

    private static final Logger LOG = Logger.getLogger(Clear4jTest.class.getName());

    @BeforeClass
    public static void setup(){
        FileUtils.writeTextToFile(TestConfig.TEST_FILE_PATH.getValue(), TestConfig.TEST_FILE_CONTENT.getValue());
    }

    @AfterClass
    public static void tearDown(){
        FileUtils.removeFile(TestConfig.TEST_FILE_PATH.getValue());
    }

    @Test
    public void testSimpleWorkFlow() throws Exception {

        // under the bonnet: we send an instruction message "load" to the file processor
        // with payload Payload.FILE_PATH = "/tmp/test.txt"
        // instruct(Enum 'The' -> selection: FileProcessor) returns a processor.FileProcessor
        // with certain methods that create instruction messages to be put on the file processor queue.
        // this queue can be local to the jvm or distributed.
        File aFile = new File(TestConfig.TEST_FILE_PATH.getValue());

        // start the workflow process
//        Workflow workflow = Clear.instruct(The.FILE_PROCESSOR).to(Instruction.LOAD_A_FILE, TestConfig.TEST_FILE_PATH.getValue());
        //TODO Key enum for "path"
        Instruction<String> instruction = Clear.send("path", TestConfig.TEST_FILE_PATH.getValue()).toAndWait(The.FILE_PROCESSOR);
        
        //TODO not thread safe
        ConcurrentHashMap<String, Serializable> map = instruction.get().getPayload();
        String text1 = (String) map.get("text"); //TODO Key enum for "text"

        // load it the boring way
        String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        // assert same content
        Assert.assertEquals(text2, text1);

    }

}
