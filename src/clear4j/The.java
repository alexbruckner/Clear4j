package clear4j;

import clear4j.msg.queue.Host;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:45
 */
public enum The {      //TODO don't make this static but have a directory of defined processors (system adapaters)

    FILE_PROCESSOR(clear4j.processors.FileProcessor.class, Host.LOCAL_HOST), //TODO remote processor + TODO remove dependency of implementation here? also don't use enum if client wan't custom processors
    FINAL_PROCESSOR(clear4j.processors.FinalProcessor.class, Host.LOCAL_HOST);
    
    private final Class<?> processorClass;
    private final Host host;
    
    The(Class<?> processorClass, Host host) {
        this.processorClass = processorClass;
        this.host = host;
    }

    public Class<?> getProcessorClass() {
        return processorClass;
    }

    public Host getHost() {
        return host;
    }
}
