package clear4j;

import clear4j.msg.queue.Host;
import clear4j.processor.instruction.Instruction;
import clear4j.processors.WorkflowProcessor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Workflow implements Serializable {

	private static final long serialVersionUID = 1L;
	private final List<Instruction<?>> instructions;
    private Instruction<?> currentInstruction;

    private int currentInstructionPosition = 0;
    
    private static final AtomicLong instanceCount = new AtomicLong();

    private final String id;
    
    public Workflow(final Serializable initialValue, Function[] functions){
    	this(initialValue, functions[0], Arrays.copyOfRange(functions, 1, functions.length));
    }
    
    public Workflow(Function firstFunction, Function... moreFunctions){
    	this(null, firstFunction, moreFunctions);
    }
    
    public Workflow(final Serializable initialValue, Function firstFunction, Function... moreFunctions){
        this.instructions = new CopyOnWriteArrayList<Instruction<?>>();

        this.instructions.add(Instruction.define(Functions.initialProcess()));

        this.instructions.add(Instruction.define(firstFunction, initialValue));

        for (Function function : moreFunctions){
        	this.instructions.add(Instruction.define(function));
        }

        this.instructions.add(Instruction.define(Functions.finalProcess()));

        this.id = String.format("%s-%s-%s", Host.LOCAL_HOST, System.currentTimeMillis(), instanceCount.addAndGet(1));

    }

    public String getId() {
        return id;
    }

    public synchronized Instruction<?> getNextInstruction() {
        if (currentInstructionPosition < instructions.size()){
            return currentInstruction = instructions.get(currentInstructionPosition++);
        } else {
            return null;
        }
    }

    public synchronized <T extends Serializable> Instruction<?> getNextInstruction(T value) {
    	if (currentInstructionPosition < instructions.size()){
            Instruction<?> instr = currentInstruction = instructions.get(currentInstructionPosition++);
            currentInstruction = new Instruction<T>(instr.getFunction(), value);
            return currentInstruction;
        } else {
            return null;
        }
    }

    public synchronized Instruction<?> getCurrentInstruction() {
        return currentInstruction;
    }

    public Instruction<?> getLastInstruction() {
    	return currentInstruction = instructions.get(instructions.size() - 1);
    }
    
    @Override
    public String toString() {
        return String.format("Workflow{id=%s, instructions=%s}", id, instructions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Workflow workflow = (Workflow) o;

        return id.equals(workflow.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

	@SuppressWarnings("unchecked")
	public <T extends Serializable> T waitFor() {
		WorkflowProcessor.waitFor(id);
		return (T) this.getCurrentInstruction().getValue();
	}
	
}
