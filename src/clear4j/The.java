package clear4j;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:45
 */
public enum The implements Processor {      //TODO don't make this static but have a directory of defined processors (system adapaters)

    FileProcessor("clear4j.processors.FileProcessor"); 

    private String processorClass;
    
    The(String processorClass) {
    	this.processorClass = processorClass;
    }

	@Override
	public Workflow to(Instruction doSomething) {
		// TODO Auto-generated method stub
		return null;
	}

}
