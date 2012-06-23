package clear4j.msg.queue.managers.remote;

import clear4j.msg.Message;
import clear4j.msg.queue.managers.QueueManager;

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
