package clear4j;

import clear4j.msg.queue.Host;
import clear4j.processor.Arg;

import java.io.Serializable;

public class Function implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Class<?> processorClass;
    private final Host host;
    private final String operation;
    private final Arg<?>[] args;
    
    public Function(Class<?> processorClass, Host host, String operation, Arg<?>... args) {
        this.processorClass = processorClass;
        this.host = host;
        this.operation = operation;
        this.args = args;
    }
    
    public static Function withArgs(final Function function, final Arg<?>... args){
    	return new Function(function.getProcessorClass(), function.getHost(), function.getOperation(), args);
    }

	public Class<?> getProcessorClass() {
		return processorClass;
	}

	public Host getHost() {
		return host;
	}

	public String getOperation() {
		return operation;
	}
	
    public Arg<?>[] getArgs() {
		return args;
	}

	@Override
    public String toString() {
        return String.format("Function{processorClass=%s, host=%s, operation='%s'}", processorClass, host, operation);
    }
}
