package clear4j.processor.instruction;

import clear4j.The;

import java.io.Serializable;

public class PipedInstruction<T extends Serializable> extends Instruction<T> {
    
	private static final long serialVersionUID = 1L;

	public PipedInstruction(final The processor, final String operation) {
        super(new Function(processor, operation), null);
    }
    
    @Override
    public String toString() {
        return String.format("Instruction{function=%s}", function);
    }
}
