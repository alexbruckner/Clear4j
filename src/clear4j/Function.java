package clear4j;

import java.io.Serializable;

public class Function implements Serializable {

    final The processor;
    final String operation;

    public Function(final The processor, final String operation){
        this.processor = processor;
        this.operation = operation;
    }

    public The getProcessor() {
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
