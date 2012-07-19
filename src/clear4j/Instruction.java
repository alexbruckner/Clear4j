package clear4j;

import java.io.Serializable;

public class Instruction<T extends Serializable>  implements Serializable {

	private static final long serialVersionUID = 1L;
	final Function function;
    final T value;

    public Instruction(Function operation, final T value){
        this.function = operation;
        this.value = value;
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
