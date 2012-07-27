package clear4j.processor.instruction;

import clear4j.beans.Function;

import java.io.Serializable;

public class PipedInstruction<T extends Serializable> extends Instruction<T> {
    
	private static final long serialVersionUID = 1L;

	public PipedInstruction(final Function function) {
        super(function, null);
    }
    
    @Override
    public String toString() {
        return String.format("Instruction{function=%s}", function);
    }
}
