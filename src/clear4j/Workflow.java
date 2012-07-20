package clear4j;

import clear4j.processor.instruction.Instruction;
import clear4j.processor.instruction.PipedInstruction;

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
    private Iterator<Instruction> iterator;
    private Instruction currentInstruction;
    private Instruction finalInstruction;

    public Workflow(final Instruction start, final PipedInstruction... pipedInstructions){
        this.instructions = new CopyOnWriteArrayList<Instruction>();
        this.instructions.add(start);
        this.values = new ConcurrentHashMap<String, Serializable>();
        Collections.addAll(this.instructions, pipedInstructions);

        finalInstruction = Instruction.to(The.FINAL_PROCESSOR, "finalProcess");
        this.instructions.add(finalInstruction);

        iterator = this.instructions.iterator();

    }

    public Instruction getNextInstruction() {
        if (iterator.hasNext()){
            return currentInstruction = iterator.next();
        } else {
            return null;
        }
    }

    public <T extends Serializable> Instruction getNextInstruction(T value) {
        if (iterator.hasNext()){
            Instruction instr = currentInstruction = iterator.next();
            currentInstruction = new Instruction<T>(instr.getFunction(), value);
            return currentInstruction;
        } else {
            return null;
        }
    }

    public Instruction getCurrentInstruction() {
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


}
