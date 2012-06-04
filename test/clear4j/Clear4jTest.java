package clear4j;

import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.Receiver;
import clear4j.msg.queue.Queue;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class Clear4jTest {

    private static final Logger LOG = Logger.getLogger(Clear4jTest.class.getName());


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

        if (LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, "starting testMessaging test");
        }

        Random random = new Random();

        final String[] checkReceived = new String[1];

        if (LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, "creating receiver");
        }

        Messenger.Receiver receiver = Messenger.register(new Receiver() {
            @Override
            public void onMessage(Message message) {
                checkReceived[0] = message.getMessage();
            }
        }).to(Queue.TEST_QUEUE);

        if (LOG.isLoggable(Level.INFO)){
            LOG.log(Level.INFO, "starting loop");
        }

        for (int i = 0; i <  1000; i++){

            String sent = "test-" + random.nextInt(1000);

            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("sending [%s]", sent));
            }

            Messenger.send(sent).to(Queue.TEST_QUEUE);

            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("sent [%s] and waiting for message.\n", sent));
            }

            receiver.waitForOneMessage();

            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("waiting over for [%s].\n", sent));
            }

            String received = checkReceived[0];

            if (LOG.isLoggable(Level.INFO)){
                LOG.log(Level.INFO, String.format("asserting [%s] == [%s].\n", sent, received));
            }

            Assert.assertEquals(sent, received);
        }
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
