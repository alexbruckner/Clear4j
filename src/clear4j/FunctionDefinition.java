package clear4j;

import clear4j.msg.queue.Host;

public class FunctionDefinition {

	private final Class<?> processorClass;
    private final Host host;
    private final String operation;
    
    public FunctionDefinition(Class<?> processorClass, Host host, String operation) {
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

}
