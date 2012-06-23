package clear4j.processors;

import clear4j.FileUtils;
import clear4j.processor.Instruction;
import clear4j.processor.Processor;

@Processor
public class FileProcessor {
	
	@Instruction
	public String loadText(String path){
		return FileUtils.loadTextFromFile(path);
	}
	
}
