package clear4j.msg;

import clear4j.msg.beans.DefaultMessage;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.beans.DefaultReceiver;
import clear4j.msg.beans.RemoteReceiver;
import clear4j.msg.beans.monitor.MonitorFrame;
import clear4j.msg.queue.*;
import clear4j.msg.queue.beans.HostPort;
import clear4j.msg.queue.management.QueueManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 25/05/12
 * Time: 14:41
 */
public final class Messenger {
    private Messenger() {
    }

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final Logger LOG = Logger.getLogger(Messenger.class.getName());

    private static MonitorFrame monitorFrame;

    /*
     * MONITORING
     */

    public static void monitorOn(int frequencyInSeconds, String... queues) {
        monitorOff();
        monitorFrame = new MonitorFrame(frequencyInSeconds * 1000, queues);
    }

    public static void monitorOff(){
        if (monitorFrame != null) {
            monitorFrame.dispose();
        }
    }


    /*
    * SENDING
    */

    public static <T extends Serializable> void send(final String queue, final T payload) {
        send(new DefaultQueue(queue, Host.LOCAL_HOST), payload);
    }

    public static <T extends Serializable> void send(final String host, final int port, final String queue, final T payload) {
        send(new DefaultQueue(queue, new HostPort(host, port)), payload);
    }

    public static <T extends Serializable> void send(final Queue target, final T payload) {
        send(new DefaultMessage<T>(target, payload));
    }

    private static <T extends Serializable> void send(final Message<T> message) {
        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("sending message: %s", message));
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {

                if (message.isLocal()) {
                    QueueManager.add(message);
                } else {
                    try {
                        Host targetHost = message.getTarget().getHost();
                        Socket socket = new Socket(targetHost.getHost(), targetHost.getPort());
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        out.flush();
                        out.writeObject(message);
                        out.flush();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        LOG.log(Level.SEVERE, e.getMessage()); //TODO put on local exception queue
                    }
                }

            }
        });

    }

    public static <T extends Serializable> Receiver<T> register(final String target, final MessageListener<T> listener) {
        return register(new DefaultQueue(target, Host.LOCAL_HOST), listener);
    }

    public static <T extends Serializable> Receiver<T> register(final String host, final int port, final String target, final MessageListener<T> listener) {
        return register(new DefaultQueue(target, new HostPort(host, port)), listener);
    }

    /*
    * RECEIVING
    */
    private static <T extends Serializable> Receiver<T> register(final Queue target, final MessageListener<T> listener) {

        Receiver<T> receiver = new DefaultReceiver<T>(target, listener);

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("registering receiver: %s", receiver));
        }

        if (receiver.isLocal()) {
            QueueManager.add(receiver);
        } else {
            //create local proxy queue for this receiver.
            Queue localProxyQueue = new DefaultQueue(receiver.getId(), Host.LOCAL_HOST);
            Receiver<T> localProxy = register(localProxyQueue, listener);
            // decorate receiver with local proxy
            receiver = new RemoteReceiver<T>(receiver, localProxy);
            //send request to remote host, ie put a receiver message to its receivers queue
            //this message will get picked up by the RemoteAdapter and a ('local' to the remote host) receiver created.
            //which proxies all messages received back to the localProxyQueue.
            Queue remoteReceivers = new DefaultQueue("remote-receivers", target.getHost());
            send(new DefaultMessage<Receiver<T>>(remoteReceivers, receiver));
        }

        return receiver;
    }

    public static <T extends Serializable> void unregister(final Receiver<T> receiver) {

        if (LOG.isLoggable(Level.INFO)) {
            LOG.log(Level.INFO, String.format("un-registering receiver: %s", receiver));
        }

        if (receiver.isLocal()) {
            QueueManager.remove(receiver);
        } else {
            RemoteReceiver<T> remote = (RemoteReceiver<T>) receiver;
            //unregister remote receiver
            Queue remoteReceivers = new DefaultQueue("unregister-receivers", receiver.getTarget().getHost());
            send(new DefaultMessage<Receiver<T>>(remoteReceivers, receiver));
            // unregister local proxy
            unregister(remote.getLocalProxy());
        }

    }

}
