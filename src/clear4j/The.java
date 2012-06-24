package clear4j;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:45
 */
public enum The implements Processor {      //TODO don't make this static but have a directory of defined processors (system adapaters)

    FILE_PROCESSOR(clear4j.processors.FileProcessor.class); 
    
    static {
    	//TODO create (local only) listeners to in-queues for all defined processors 
    	//TODO which will then call the relevant method required.
    }

    private final Class processorClass;
    
    The(final Class processorClass) {
    	this.processorClass = processorClass;
    }

	@Override
	public Workflow to(Instruction doSomething) {
		// TODO send a message to the in-queue for the processor. 
		return null;
	}

}
