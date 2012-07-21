package clear4j;

public enum Def implements FunctionDefinition {
	
	LOAD_TEXT(The.FILE_PROCESSOR, "loadText");
	
	private final ProcessorDefinition processor;
	private final String operation;
	
	private Def(ProcessorDefinition processor, String operation) {
		this.processor = processor;
		this.operation = operation;
	}

	@Override
	public ProcessorDefinition getProcessor() {
		return processor;
	}

	@Override
	public String getOperation() {
		return operation;
	}
	
}
