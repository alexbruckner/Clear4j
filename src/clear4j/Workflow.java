package clear4j;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Workflow implements Serializable {

    private final List<Instruction> instructions;
    private final Map<String, Serializable> values; //only used to record all goings on within a workflow, value passing is done in clear directly
    private final Iterator<Instruction> iterator;
    private Instruction currentInstruction;

    public Workflow(final Instruction start, final Instruction... instructions){
        this.instructions = new CopyOnWriteArrayList<Instruction>();
        this.instructions.add(start);
        Collections.addAll(this.instructions, instructions);
        this.values = new ConcurrentHashMap<String, Serializable>();
        iterator = this.instructions.iterator();
    }

    public Instruction getNextInstruction() {
        return currentInstruction = iterator.next();
    }

    public Instruction getCurrentInstruction() {
        return currentInstruction;
    }

    public <T extends Serializable> void setValue(String key, T value){
        values.put(key, value);
    }

    public Map<String, Serializable> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return String.format("Workflow{instructions=%s, values=%s}", instructions, values);
    }
}
