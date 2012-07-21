package clear4j;

import java.io.Serializable;

import clear4j.msg.queue.Host;

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

}
