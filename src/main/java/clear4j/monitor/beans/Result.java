package clear4j.monitor.beans;

import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.processor.instruction.Instruction;

import java.io.Serializable;

public class Result implements Serializable {

    private final String result;

    public Result(final Workflow workflow) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WORKFLOW[%s]%n", workflow.getId()));
        for (Instruction<?> instruction : workflow.getInstructions()){
            Serializable pipedValue = instruction.getValue();
            Function function = instruction.getFunction();
            sb.append(String.format("___%s->%s.%s(%s) : %s %n", function.getHost(), function.getProcessorClass().getName(), function.getOperation(), pipedValue, instruction.isDone() ? "DONE" : "-")); //TODO args
        }
        sb.append(String.format("%n"));
        result = sb.toString();
    }

    @Override
    public String toString() {
        return result;
    }
}
