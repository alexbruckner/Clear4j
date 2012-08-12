package clear4j.beans;

import clear4j.msg.queue.Host;
import clear4j.processor.Arg;

import java.io.Serializable;

public class Function implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Class<?> processorClass;
    private final Host host;
    private final String operation;
	private final Class<?> runtimeArgumentType;
    private final Arg<?>[] args;
    
    public Function(Class<?> processorClass, String operation, Class<?> runtimeArgumentType, Arg<?>... args) {
		this(Host.LOCAL_HOST, processorClass, operation, runtimeArgumentType, args);
	}

	public Function(Host host, Class<?> processorClass, String operation, Class<?> runtimeArgumentType, Arg<?>... args) {
        this.processorClass = processorClass;
        this.host = host;
        this.operation = operation;
		this.runtimeArgumentType = runtimeArgumentType;
        this.args = args;
    }
    
    public static Function withArgs(final Function function, final Arg<?>... args){
    	return new Function(function.getHost(), function.getProcessorClass(), function.getOperation(), function.getRuntimeArgumentType(), args);
    }

    public static <T extends Serializable> Function withArg(final Function function, String key, T value){
        return new Function(function.getHost(), function.getProcessorClass(), function.getOperation(), function.getRuntimeArgumentType(), new Arg<T>(key, value));
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

	public Class<?> getRuntimeArgumentType() {
		return runtimeArgumentType;
	}

	public Arg<?>[] getArgs() {
		return args;
	}

	@Override
    public String toString() {
        return String.format("Function{processorClass=%s, host=%s, operation='%s'}", processorClass, host, operation);
    }
}
