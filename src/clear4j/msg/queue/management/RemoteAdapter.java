package clear4j.msg.queue.management;

import clear4j.msg.Messenger;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;

import java.io.IOException;
import java.io.ObjectInputStream;
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
    private static final int PORT = 9876;

    public RemoteAdapter() {

        try {
            final ServerSocket serverSocket = new ServerSocket(PORT);

            // create remote-receivers queue listener
            //TODO
            // expected message format: (remoteHost/remotePort/remoteQueue)
            // where remoteQueue = (localHost/localPort/localQueue);
            Messenger.register("remote-receivers", new MessageListener<String>() {
                @Override
                public void onMessage(Message<String> message) {

                    String msg = message.getPayload();

                    int index;

                    final String remoteHost = msg.substring(1, (index = msg.indexOf("/")));
                    final int remotePort = Integer.parseInt(msg.substring(index + 1, (index = msg.indexOf("/", index + 1))));
                    final String remoteQueue = msg.substring(index + 1, msg.length() - 1);
                    final String localQueue = remoteQueue.substring(remoteQueue.lastIndexOf("/") + 1, remoteQueue.length() - 1);

                    Messenger.register(localQueue, new MessageListener<String>() {
                        @Override
                        public void onMessage (Message<String> message) {
                            //TODO fix this
//                            Message<String> msg = Messenger.send(message.getPayload());
//                            msg.on(remoteHost, remotePort).to(remoteQueue);
                        }
                    });
                }
            });

            
            // create server socket to listen for messages from the beyond
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
                                        Message received = (Message) in.readObject();
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

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }

    }
}
