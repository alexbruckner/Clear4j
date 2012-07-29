package clear4j;

import java.io.Serializable;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.processor.instruction.Instruction;

public final class Runner {
	private Runner(){
	}

    public static void run(Workflow workflow, Instruction<?> instr) {
        if (instr != null) {
            Function function = instr.getFunction();
            Messenger.send(new DefaultQueue(function.getProcessorClass().getName(), function.getHost()), workflow);
        }
    }

    public static void run(Workflow workflow, Serializable returnValue) {
        run(workflow, workflow.getNextInstruction(returnValue));
    }
    
    public static void runFinalProcess(Workflow workflow) {
        run(workflow, workflow.getLastInstruction());
    }
}
