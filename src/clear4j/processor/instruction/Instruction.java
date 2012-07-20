package clear4j.processor.instruction;

import clear4j.The;

import java.io.Serializable;

public class Instruction<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    final Function function;
    final T value;

    public Instruction(Function operation, final T value) {
        this.function = operation;
        this.value = value;
    }

    public static <T extends Serializable> Instruction<T> to(final The processor, final String operation, final T value) {
        return new Instruction<T>(new Function(processor, operation), value);
    }

    public static <T extends Serializable> PipedInstruction<T> to(final The processor, final String operation) {
        return new PipedInstruction<T>(processor, operation);
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