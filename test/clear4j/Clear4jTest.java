package clear4j;

import org.junit.Test;
import sun.awt.windows.ThemeReader;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class Clear4jTest {

    @Test
    public void testScenario(){

        // under the bonnet: we send an instruction message "load" to the file processor with payload "file"
        // instruct(Enum 'The' -> seclection: FileProcessor) returns a processor.FileProcessor
        // with certain methods that create instruction messages to be put on the file processor queue.
        // this queue can be local to the jvm or distributed.
        Clear.instruct(The.FileProcessor).toLoad(aFile);


    }

}
