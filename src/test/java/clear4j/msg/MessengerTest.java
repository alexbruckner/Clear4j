package clear4j.msg;

import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.msg.queue.Receiver;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 09:23
 */
public class MessengerTest {

    private static final Logger LOG = Logger.getLogger(MessengerTest.class.getName());

    @BeforeClass
    public static void init(){
        Messenger.monitorOn(1, "test", "test-remote");
    }

    @AfterClass
    public static void cleanUp(){
        Messenger.monitorOff();
    }
    
    @Test
    public void testExceptionInReceiver() throws InterruptedException {
    	Messenger.register("test", new MessageListener<String>() {
            @Override
            public void onMessage(Message<String> message) {
                System.out.println("message received");
            	throw new RuntimeException("test exception thrown in receiver");
            }
        });
    	Messenger.send("test", "payload");
    }

    @Test
    public void testRemoteReceiver() throws InterruptedException, ExecutionException {

        final String[] targetQueueName = new String[1];

        Receiver<String> remote = Messenger.register("localhost", 9876, "test-remote", new MessageListener<String>() {
            @Override
            public void onMessage(Message<String> message) {
                targetQueueName[0] = message.getTarget().getName();
            }
        });

        Thread.sleep(2000);

        Messenger.send("test-remote", "payload");

        Thread.sleep(2000);

        Assert.assertEquals("The remote receiver should have sent the message back to the local proxy queue of name = receiver.id", remote.getId(), targetQueueName[0]);

        Messenger.unregister(remote);

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

        Receiver<String> receiver = Messenger.register("test", new MessageListener<String>() {
            private int count;

            @Override
            public void onMessage(Message<String> message) {
                receivedMessages.add(message.getPayload());
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, String.format("messages received: %s", ++count));
                }
            }
        });

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "starting loop");
        }

        for (int i = 0; i < NUM; i++) {

            String sent = "test-" + random.nextInt(1000);

            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, String.format("sending [%s]", sent));
            }

            sentMessages.add(sent);
            Messenger.send("test", sent);
        }

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, "waiting for all messages");
        }

        Thread.sleep(2000);

        for (String sent : sentMessages) {
            Assert.assertTrue(String.format("%s not in received messages!", sent), receivedMessages.contains(sent));
        }

        boolean ordered = true;
        for (int i = 0; i < sentMessages.size(); i++) {
            if (!receivedMessages.get(i).equals(sentMessages.get(i))){
                ordered = false;
                break;
            }
        }
        Assert.assertFalse("should not be ordered", ordered);

        Messenger.unregister(receiver);
        
    }

}
