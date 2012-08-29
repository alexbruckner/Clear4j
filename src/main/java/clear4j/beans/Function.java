package clear4j.beans;

import clear4j.msg.queue.Host;

import java.io.Serializable;

public class Function implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Class<?> processorClass;
	private final Host host;
	private final String operation;
	private final Class<?> runtimeArgumentType;

	public Function(Class<?> processorClass, String operation, Class<?> runtimeArgumentType) {
		this(Host.LOCAL_HOST, processorClass, operation, runtimeArgumentType);
	}

	public Function(Host host, Class<?> processorClass, String operation, Class<?> runtimeArgumentType) {
		this.processorClass = processorClass;
		this.host = host;
		this.operation = operation;
		this.runtimeArgumentType = runtimeArgumentType;
	}

	public Function(Class<?> processorClass, String operation) {
		this(processorClass, operation, null);
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

	@Override
	public String toString() {
		return String.format("Function{processorClass=%s, host=%s, operation='%s'}", processorClass, host, operation);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Function)) return false;

		Function function = (Function) o;

		if (!host.equals(function.host)) return false;
		if (!operation.equals(function.operation)) return false;
		if (!processorClass.equals(function.processorClass)) return false;

		if (runtimeArgumentType != null && !runtimeArgumentType.equals(function.runtimeArgumentType)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = processorClass.hashCode();
		result = 31 * result + host.hashCode();
		result = 31 * result + operation.hashCode();
		if (runtimeArgumentType != null)
			result = 31 * result + runtimeArgumentType.hashCode();
		return result;
	}
}
