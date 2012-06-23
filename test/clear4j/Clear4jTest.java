package clear4j;

import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.Receiver;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
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

//    @Test
//    public void testMessaging100() throws Exception {
//        for (int i = 0; i < 100; i++){
//            testMessaging();
//        }
//    }
    
    @Test
    public void testRemoteReceiver() throws Exception { 
    	
    	String remoteQueue = "remote queue";
        String remoteMessage = "remote test";
        
    	Messenger.register(new Receiver(){
            @Override
            public void onMessage(Message message) {
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, String.format("messages received: %s", message));
                }
            }
        }).on("localhost", 9876).to(remoteQueue);
    	
    	Messenger.send(remoteMessage).on("localhost", 9876).to(remoteQueue);
    	
    	Thread.sleep(1000);
    	
    }

    @Test
    public void testRemoteAdapter() throws Exception {

        String localQueue = "local queue";
        String localMessage = "local test";
        String remoteQueue = "remote queue";
        String remoteMessage = "remote test";

        Future<Message> remoteValue = Messenger.register(remoteQueue);
        Messenger.send(remoteMessage).on("localhost", 9876).to(remoteQueue);
        Message remote = remoteValue.get();
        Assert.assertEquals(remoteMessage, remote.getMessage());

        Future<Message> localValue = Messenger.register(localQueue);
        Messenger.send(localMessage).to(localQueue);
        Message local = localValue.get();
        Assert.assertEquals(localMessage, local.getMessage());

    }


    @Test
    public void testMessaging() throws Exception {

        int NUM = 1000;

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "starting testMessaging test");
        }

        Random random = new Random();

        final List<String> sentMessages = new CopyOnWriteArrayList<String>();
        final List<String> receivedMessages = new CopyOnWriteArrayList<String>();

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "creating receiver");
        }

        Messenger.Receiver receiver = Messenger.register(new Receiver() {
            private int count;

            @Override
            public void onMessage(Message message) {
                receivedMessages.add(message.getMessage());
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, String.format("messages received: %s", ++count));
                }
            }
        }).to("test");

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "starting loop");
        }

        for (int i = 0; i < NUM; i++) {

            String sent = "test-" + random.nextInt(1000);

            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("sending [%s]", sent));
            }

            sentMessages.add(sent);
            Messenger.send(sent).to("test");
        }

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "waiting for all messages");
        }

        Messenger.waitFor("test");

        for (String sent : sentMessages) {
            Assert.assertTrue(String.format("%s not in received messages!", sent), receivedMessages.contains(sent));
        }

        Messenger.unregister(receiver);
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
