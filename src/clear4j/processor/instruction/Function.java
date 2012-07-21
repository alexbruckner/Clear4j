package clear4j.processor.instruction;

import clear4j.ProcessorDefinition;

import java.io.Serializable;

public class Function implements Serializable {

	private static final long serialVersionUID = 1L;
	
	final ProcessorDefinition processor;
    final String operation;

    public Function(final ProcessorDefinition processor, final String operation){
        this.processor = processor;
        this.operation = operation;
    }

    public ProcessorDefinition getProcessor() {
        return processor;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public String toString() {
        return String.format("Function{processor=%s, operation='%s'}", processor, operation);
    }
}
