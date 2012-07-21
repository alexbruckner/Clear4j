package clear4j.processor.instruction;

import clear4j.ProcessorDefinition;

import java.io.Serializable;

public class PipedInstruction<T extends Serializable> extends Instruction<T> {
    
	private static final long serialVersionUID = 1L;

	public PipedInstruction(final ProcessorDefinition processor, final String operation) {
        super(new Function(processor, operation), null);
    }
    
    @Override
    public String toString() {
        return String.format("Instruction{function=%s}", function);
    }
}
