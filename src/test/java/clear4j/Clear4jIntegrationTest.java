package clear4j;


import clear4j.beans.Function;
import clear4j.config.Functions;
import clear4j.config.TestFunctions;
import clear4j.config.Workflows;
import clear4j.processor.Arg;
import org.junit.Test;


public class Clear4jIntegrationTest extends Clear4jTest {

    @Override
    protected Function[] getFunctions(){
        return new Function[]{ 
    		Functions.loadText(), Function.withArgs(TestFunctions.println(), new Arg<String>("test-key", "test-value"))
        };
    }

    @Test
    public void testWorkflowMonitor() throws InterruptedException {
        // starts a workflow here that instructs the remote machine to start a workflow with remote println, which in this case is local to the remote machine.
        Clear.run(Workflows.runWorkflowRemotely("localhost", 7777, Workflows.remotePrintln("localhost", 7777, "test value PRINT")));   //TODO clean up
        Thread.sleep(1000);
        Clear.run(Workflows.getMonitorWorkflow("localhost", 7777)).waitFor();
    }

}
