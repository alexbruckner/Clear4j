package clear4j;

import clear4j.processor.instruction.Instruction;
import clear4j.processor.instruction.PipedInstruction;
import clear4j.processors.FileUtils;
import junit.framework.Assert;
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

        // start the workflow process
        // TODO convinience methods
        // TODO have proc.func function object + args object. create a print processor for FileProcessor.loadText('file')->PrintProcessor.print [actual language would have to be loadText(file)->print]

        Instruction<String> loadText = Instruction.to(The.FILE_PROCESSOR, "loadText", TestConfig.TEST_FILE_PATH.getValue());
        PipedInstruction<String> print = Instruction.to(The.PRINT_PROCESSOR, "println");
        Workflow workflow = new Workflow(loadText, print);
        Clear.run(workflow);

        // wait for the result
//        String text1 = workflow.waitFor();

        // load it the boring way
        //String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        // assert same content
//        Assert.assertEquals(text2, text1);
    	
    	Thread.sleep(5000);

        Assert.fail("implement some waitfor method that checks data in the final processor.");

    }

}
