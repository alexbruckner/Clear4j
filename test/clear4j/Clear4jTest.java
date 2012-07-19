package clear4j;

import clear4j.processors.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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

        // start the workflow process
        // TODO convinience methods
        // TODO have proc.func function object + args object. create a print processor for FileProcessor.loadText('file')->PrintProcessor.print [actual language would have to be loadText(file)->print]

        Clear.run(new Instruction(new Function(The.FILE_PROCESSOR, "loadText"), new Param("path", TestConfig.TEST_FILE_PATH.getValue())));
       
        
        //TODO not thread safe
        //ConcurrentHashMap<String, Serializable> map = instruction.get().getPayload();
        //String text1 = (String) map.get("text"); //TODO Key enum for "text"

        // load it the boring way
        //String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        // assert same content
        //Assert.assertEquals(text2, text1);
    	
    	Thread.sleep(5000);

    }

}
