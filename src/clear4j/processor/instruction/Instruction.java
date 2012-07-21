package clear4j.processor.instruction;

import clear4j.FunctionDefinition;
import clear4j.ProcessorDefinition;

import java.io.Serializable;

public class Instruction<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    final Function function;
    final T value;

    public Instruction(Function operation, final T value) {
        this.function = operation;
        this.value = value;
    }

    public static <T extends Serializable> Instruction<T> to(final ProcessorDefinition processor, final String operation, final T value) {
        return new Instruction<T>(new Function(processor, operation), value);
    }

    public static <T extends Serializable> Instruction<T> define(final FunctionDefinition function, final T value) {
        return new Instruction<T>(new Function(function.getProcessor(), function.getOperation()), value);
    }
    
    public static <T extends Serializable> PipedInstruction<T> to(final ProcessorDefinition processor, final String operation) {
        return new PipedInstruction<T>(processor, operation);
    }
    
    public static <T extends Serializable> PipedInstruction<T> define(final FunctionDefinition function) {
        return new PipedInstruction<T>(function.getProcessor(), function.getOperation());
    }


    public Function getFunction() {
        return function;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Instruction{function=%s, value=%s}", function, value);
    }

}
