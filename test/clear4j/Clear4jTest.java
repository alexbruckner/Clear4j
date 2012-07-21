package clear4j;

import clear4j.processor.instruction.Instruction;
import clear4j.processor.instruction.PipedInstruction;
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

    @Test
    public void testSimpleWorkFlow() throws Exception {

        // start the workflow process //TODO merge Instruction and function definition or need this for custom functions?
        Instruction<String> loadText = Instruction.define(Functions.loadText(), TestConfig.TEST_FILE_PATH.getValue());
        PipedInstruction<String> print = Instruction.define(Functions.println());
        Workflow workflow = new Workflow(loadText, print);
        Clear.run(workflow);

        // wait for the result
        String text1 = workflow.waitFor();

        // load it the boring way
        String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        // assert same content
        Assert.assertEquals(text2, text1);
    	
    }

}
