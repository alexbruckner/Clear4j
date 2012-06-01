package clear4j;

import clear4j.msg.Receiver;
import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.queue.Queue;
import junit.framework.Assert;
import org.junit.Test;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class Clear4jTest {

//    @BeforeClass
//    public static void setup(){
//        FileUtils.writeTextToFile(TestConfig.TEST_FILE_PATH.getValue(), TestConfig.TEST_FILE_CONTENT.getValue());
//    }
//
//    @AfterClass
//    public static void tearDown(){
//        FileUtils.removeFile(TestConfig.TEST_FILE_PATH.getValue());
//    }

    @Test
    public void testMessaging(){

        String sent = "test";

        final String[] msg = new String[1];

        Messenger.Receiver receiver = Messenger.register(new Receiver() {
            @Override
            public void onMessage(Message message) {
                msg[0] = message.getMessage();
            }
        }).to(Queue.TEST_QUEUE);

        Messenger.send(sent).to(Queue.TEST_QUEUE);

        receiver.waitForOneMessage();

        String received = msg[0];

        Assert.assertEquals(sent, received);

    }

//    @Test
//    public void testSimpleWorkFlow() {
//
//        // under the bonnet: we send an instruction message "load" to the file processor
//        // with payload Payload.FILE_PATH = "/tmp/test.txt"
//        // instruct(Enum 'The' -> selection: FileProcessor) returns a processor.FileProcessor
//        // with certain methods that create instruction messages to be put on the file processor queue.
//        // this queue can be local to the jvm or distributed.
//        File aFile = new File(TestConfig.TEST_FILE_PATH.getValue());
//
//        // start the workflow process
//        Workflow workflow = Clear.instruct(The.FileProcessor).toLoad(aFile);
//        Payload payload = workflow.waitFor();
//        String text1 = payload.get(Payload.TEXT);
//
//        // load it the boring way
//        String text2 = FileUtils.loadTextFromFile(TestConfig.TEST_FILE_PATH.getValue());
//
//        // assert same content
//        Assert.assertEquals(text2, text1);
//
//    }



}
