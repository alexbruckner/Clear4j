package clear4j.processor.instruction;

import clear4j.beans.Function;

import java.io.Serializable;

public class Instruction<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    protected final Function function;
    private final T value;
    private volatile boolean done;
    private volatile Exception exception;

    public Instruction(Function operation, final T value) {
        this.function = operation;
        this.value = value;
    }

    public static <T extends Serializable> Instruction<T> define(final Function function, final T value) {
        return new Instruction<T>(function, value);
    }
    
    public static <T extends Serializable> PipedInstruction<T> define(final Function function) {
        return new PipedInstruction<T>(function);
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
    
    public boolean isDone(){
    	return done;
    }

	public synchronized void setDone(boolean done) {
		this.done = done;
	}

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
