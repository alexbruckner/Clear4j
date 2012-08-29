package clear4j;


import clear4j.beans.Workflow;
import clear4j.config.TestFunctions;
import clear4j.config.TestWorkflows;
import clear4j.config.Workflows;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class Clear4jIntegrationTest extends Clear4jTest {

	@Override
	protected Workflow getWorkflow(String path) {
		return new Workflow(path, TestFunctions.loadText(), TestFunctions.remotePrintln());
	}

	@Test
	public void testMonitorWebserver() throws InterruptedException, IOException {
		Clear.run(Workflows.runWorkflowRemotely("localhost", 7777, TestWorkflows.remotePrintlnAndSleep("localhost", 7777, "test value PRINT")));   //TODO clean up
		Thread.sleep(3000);
		String monitorHtml = readFromURL("http://localhost:7778");
		System.out.println(monitorHtml);
		Assert.assertTrue(monitorHtml.contains("<li> <img width=10 height=10 src=\"/yellow.png\"/> clear4j.processors.PrintProcessor.println(test value PRINT)</li>"));
		Assert.assertTrue(monitorHtml.contains("<li> <img width=10 height=10 src=\"/yellow.png\"/> clear4j.processors.SleepProcessor.sleep(null)</li>"));
	}

	private static String readFromURL(String url) throws IOException {
		StringBuilder sb = new StringBuilder();
		URL oracle = new URL(url);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine).append(String.format("%n"));
		in.close();
		return sb.toString();
	}

}
