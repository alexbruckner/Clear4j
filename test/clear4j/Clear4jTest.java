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

        // start the workflow process
        // TODO convenience methods
        // TODO have proc.func function object + args object. create a print processor for FileProcessor.loadText('file')->PrintProcessor.print [actual language would have to be loadText(file)->print]

    	//can either use an instruction with 'define' using a function definition //TODO?
        Instruction<String> loadText = Instruction.define(Def.LOAD_TEXT, TestConfig.TEST_FILE_PATH.getValue());
        //or dynamically assume a processor has a function with 'to'
        PipedInstruction<String> print = Instruction.to(The.PRINT_PROCESSOR, "println");
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
