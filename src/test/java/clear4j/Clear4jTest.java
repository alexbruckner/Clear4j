package clear4j;

import clear4j.processor.Arg;
import clear4j.processors.FileUtils;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;


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
    		Functions.loadText(), Function.withArgs(Functions.println(), new Arg<String>("test-key", "test-value"))
        };
    }

    @Test
    public void testSimpleWorkFlow() throws Exception {
        System.out.println(Arrays.toString(getFunctions()));
        // start the workflow process
        String filePath = TestConfig.TEST_FILE_PATH.getValue();
        Workflow workflow = new Workflow(filePath, getFunctions());
        Clear.run(workflow);

        // wait for the result
        String text1 = workflow.waitFor();

        // load it the boring way
        String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());

        // assert same content
        Assert.assertEquals(text2, text1);
    	
    }
  
}
