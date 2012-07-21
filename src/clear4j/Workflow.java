package clear4j;

import clear4j.msg.queue.Host;
import clear4j.processor.instruction.Instruction;
import clear4j.processors.FinalProcessor;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Workflow implements Serializable {

	private static final long serialVersionUID = 1L;
	private final List<Instruction<?>> instructions;
    private final Map<String, Serializable> values; //only used to record all goings on within a workflow, value passing is done in clear directly
    private Iterator<Instruction<?>> iterator;
    private Instruction<?> currentInstruction;

    private static final AtomicLong instanceCount = new AtomicLong();

    private final String id;

    public Workflow(final Serializable initialValue, Function firstFunction, Function... moreFunctions){
        this.instructions = new CopyOnWriteArrayList<Instruction<?>>();
        this.instructions.add(Instruction.define(firstFunction, initialValue));
        this.values = new ConcurrentHashMap<String, Serializable>();
        
        for (Function function : moreFunctions){
        	this.instructions.add(Instruction.define(function));
        }

        this.instructions.add(Instruction.define(Functions.finalProcess())); //TODO lose one or the other?

        iterator = this.instructions.iterator();

        this.id = String.format("%s-%s-%s", Host.LOCAL_HOST, System.currentTimeMillis(), instanceCount.addAndGet(1));

    }

    public String getId() {
        return id;
    }

    public Instruction<?> getNextInstruction() {
        if (iterator.hasNext()){
            return currentInstruction = iterator.next();
        } else {
            return null;
        }
    }

    public <T extends Serializable> Instruction<?> getNextInstruction(T value) {
        if (iterator.hasNext()){
            Instruction<?> instr = currentInstruction = iterator.next();
            currentInstruction = new Instruction<T>(instr.getFunction(), value);
            return currentInstruction;
        } else {
            return null;
        }
    }

    public Instruction<?> getCurrentInstruction() {
        return currentInstruction;
    }

    public <T extends Serializable> void setValue(String key, T value){
        if (key != null && value != null) {
            values.put(key, value);
        }
    }

    public Map<String, Serializable> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return String.format("Workflow{instructions=%s, values=%s}", instructions, values);
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
	public synchronized <T extends Serializable> T waitFor() {
		while(FinalProcessor.getWorkflow(id) == null){
			try {
				wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		return (T) this.getCurrentInstruction().getValue();
	}
}
