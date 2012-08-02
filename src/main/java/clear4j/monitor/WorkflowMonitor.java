package clear4j.monitor;

import clear4j.Clear;
import clear4j.config.Functions;
import clear4j.config.Workflows;
import clear4j.msg.queue.management.RemoteAdapter;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class WorkflowMonitor extends Thread {
	
    private static final Logger LOG = Logger.getLogger(RemoteAdapter.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	    

    static {
        LogManager.getLogManager().reset(); //turn off logging
    }

    private final String host;
    private final int port;

    public WorkflowMonitor(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run(){
        while (!isInterrupted()){
            try {
                Clear.run(Workflows.getMonitorWorkflow(host, port)).waitFor();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private <T extends Serializable> void webserver() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(9877);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final Socket socket = serverSocket.accept();
                        executor.submit(new Runnable() {
							@Override
                            public void run() {
                            	PrintStream out = null;
                            	try {
                                    out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
                                    Serializable results = Clear.run(Functions.monitor()).waitFor();
                                    out.println(results.toString());
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                	e.printStackTrace();
                                	LOG.log(Level.SEVERE, e.getMessage());
                                } finally {
                                	if (out != null){
                                		out.close();
                                	}
                                }
                            }
                        });
                    } catch (IOException e) {
                    	e.printStackTrace();
                        LOG.log(Level.SEVERE, e.getMessage());
                    }
                }
            }
        }.start();
    }

}
