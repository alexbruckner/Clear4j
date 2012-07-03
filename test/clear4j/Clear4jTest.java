package clear4j;

import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.Receiver;
import clear4j.processors.FileUtils;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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


    @Test   //TODO not thread safe
    public void testRemoteReceiver() throws Exception { 
    	
    	String remoteQueue = "remote queue";
        String remoteMessage = "remote test";

        final String[] received = new String[1];

        Messenger.Receiver<String> receiver = Messenger.register(new Receiver<String>(){
            @Override
            public void onMessage(Message<String> message) {
                received[0] = message.getPayload();
            }
        }).on("localhost", 9876).to(remoteQueue);

        Thread.sleep(3000);   //have to wait for registration //todo

    	Messenger.send(remoteMessage).on("localhost", 9876).to(remoteQueue);

    	Messenger.waitFor(receiver.getQueue(), remoteMessage);

        Assert.assertEquals(remoteMessage, received[0]);
    	
    	Messenger.unregister(receiver);  //TODO remote unregistering when it turns out it's required..
    }

    @Test
    public void testRemoteAdapter() throws Exception {

        String localQueue = "local queue";
        String localMessage = "local test";
        String remoteQueue = "remote queue";
        String remoteMessage = "remote test";

        //Message<String> remote = Messenger.send(remoteMessage).on("localhost", 9876).toAndWait(remoteQueue); //TODO fix this!
        //Assert.assertEquals(remoteMessage, remote.getPayload());
        Assert.fail("needs fixing");

        Message<String> local = Messenger.send(localMessage).toAndWait(localQueue);
        Assert.assertEquals(localMessage, local.getPayload());

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

        Messenger.Receiver<String> receiver = Messenger.register(new Receiver<String>() {
            private int count;

            @Override
            public void onMessage(Message<String> message) {
                receivedMessages.add(message.getPayload());
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

}
