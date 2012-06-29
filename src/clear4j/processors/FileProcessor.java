package clear4j.processors;

import clear4j.processor.Instruction;
import clear4j.processor.Processor;

@Processor
public class FileProcessor {
	
	@Instruction(clear4j.Instruction.LOAD_A_FILE)
	public String loadText(String path){
		return FileUtils.loadTextFromFile(path);
	}
	
}
