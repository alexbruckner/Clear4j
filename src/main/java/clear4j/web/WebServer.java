package clear4j.web;

import clear4j.Clear;
import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.config.Functions;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.management.RemoteAdapter;
import clear4j.processor.instruction.Instruction;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
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
        
        // read html request
        String requestPath = readRequest(in);
        System.out.format("PATH: [%s]%n", requestPath);         
        
        // send the response //TODO image requests
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        printResponse(out);
        
        socket.close();

    }

	private String readRequest(BufferedReader in) throws IOException {
		// read the html headers
        System.out.println("incoming");
        String header = in.readLine();
        String request = header;
        while (!header.equals("")) {
            System.out.println(header);
            header = in.readLine();
        }
        System.out.println("done incoming.");
        return request.substring(4);
	}
	
	private void printResponse(PrintWriter out) {
        // send the headers
		out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Server: Clear4j");
        // this blank line signals the end of the headers
        out.println("");
        
        // send the HTML page
        renderHtml(out);
        
        out.flush();
	}

	private void renderHtml(PrintWriter out) {
		out.format("<h1>Clear4j monitor @ %s</h1>%n", Host.LOCAL_HOST);
        @SuppressWarnings("unchecked")
		String output = toHtml((List<Workflow>)Clear.run(Functions.monitor()).waitFor());
        out.println(output);
	}

	private synchronized String toHtml(List<Workflow> workflows) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (Workflow workflow : workflows) {
			sb.append(String.format("<li>WORKFLOW[%s]%n<ul>%n", workflow.getId()));
			List<Instruction<?>> instructions = workflow.getInstructions();
	        for (Instruction<?> instruction : instructions.subList(1, instructions.size() - 1)){
	            Serializable pipedValue = instruction.getValue();
	            Function function = instruction.getFunction();
	            sb.append(String.format("<li>%s.%s(%s) : %s</li>%n", function.getProcessorClass().getName(), function.getOperation(), pipedValue, instruction.isDone() ? "DONE" : "")); //TODO args
	        }
	        sb.append(String.format("</ul>%n</li>%n"));
		}
		sb.append("</ul>");
		return sb.toString();
	}

}
