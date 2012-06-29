package clear4j;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:45
 */
public enum The {      //TODO don't make this static but have a directory of defined processors (system adapaters)

    FILE_PROCESSOR(clear4j.processors.FileProcessor.class); //TODO remote processor + TODO remove dependency of implementation here? also don't use enum if client wan't custom processors

    private final Class processorClass;
    
    The(final Class processorClass) {
    	this.processorClass = processorClass;
    }

    public Class getProcessorClass() {
        return processorClass;
    }
}
