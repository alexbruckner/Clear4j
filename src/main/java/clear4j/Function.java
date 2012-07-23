package clear4j;

import clear4j.msg.queue.Host;

import java.io.Serializable;

public class Function implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Class<?> processorClass;
    private final Host host;
    private final String operation;
    
    public Function(Class<?> processorClass, Host host, String operation) {
        this.processorClass = processorClass;
        this.host = host;
        this.operation = operation;
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

    @Override
    public String toString() {
        return String.format("Function{processorClass=%s, host=%s, operation='%s'}", processorClass, host, operation);
    }
}
