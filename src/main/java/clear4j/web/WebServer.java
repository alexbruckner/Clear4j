package clear4j.web;

import clear4j.Clear;
import clear4j.config.Functions;
import clear4j.msg.queue.management.RemoteAdapter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebServer {

    private static final Logger LOG = Logger.getLogger(RemoteAdapter.class.getName());
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public WebServer(int port) throws IOException {
        System.out.format("Running webserver on port %s%n", port);
        webserver(port);
    }

    private <T extends Serializable> void webserver(final int port) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(port);
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
                                    handleSocket(socket);
                                } catch (IOException e) {
                                    e.printStackTrace();
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

    private void handleSocket(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String str = ".";
        while (!str.equals(""))
            str = in.readLine();

        // Send the response
        // Send the headers
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Server: Bot");
        // this blank line signals the end of the headers
        out.println("");
        // Send the HTML page
        out.println("<h1>Clear4j monitor</h1>");
        String output = String.valueOf(Clear.run(Functions.monitor()).waitFor()).replace("\n", "<br/>");
        out.println(output);
        out.flush();
        socket.close();

    }

    public static void main(String[] args) throws IOException {
        new WebServer(9877);
    }

}
