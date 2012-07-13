package clear4j.msg.queue.management;

import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.msg.queue.Receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * SERVER SOCKET for remote messages
 */
public class RemoteAdapter {

    private static final Logger LOG = Logger.getLogger(RemoteAdapter.class.getName());

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final int PORT = Host.LOCAL_HOST.getPort();

    public RemoteAdapter() {

        try {

            // create remote-receivers queue for message listener messages from beyond
            remoteReceiverRegistration();

            // create server socket to listen for messages from the beyond
            remoteMessageRegistration();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }

    }

    private <T extends Serializable> void remoteReceiverRegistration() {
        Messenger.register("remote-receivers", new MessageListener<Receiver<T>>() {
            @Override
            public void onMessage(Message<Receiver<T>> message) {

                final Receiver remoteReceiver = message.getPayload();

                Messenger.register(remoteReceiver.getTarget().getName(), new MessageListener<T>() {
                    @Override
                    public void onMessage(Message<T> message) {

                        Messenger.send(new DefaultQueue(remoteReceiver.getId(), remoteReceiver.getOrigin()), message.getPayload());

                    }
                });
            }

        });

    }

    private <T extends Serializable> void remoteMessageRegistration() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(PORT);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final Socket socket = serverSocket.accept();
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                                    Message<T> received = (Message<T>) in.readObject();
                                    QueueManager.add(received);
                                } catch (Exception e) {
                                    LOG.log(Level.SEVERE, e.getMessage());
                                }
                            }
                        });
                    } catch (IOException e) {
                        LOG.log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        }.start();
    }
}
