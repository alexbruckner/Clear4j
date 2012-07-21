package clear4j;

import clear4j.msg.queue.Host;

public interface ProcessorDefinition {
	
	String getName();
	Class<?> getProcessorClass();
	Host getHost();

}
