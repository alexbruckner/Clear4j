package clear4j.processors;

import clear4j.FileUtils;
import clear4j.Instructions;
import clear4j.The;
import clear4j.processor.Instruction;
import clear4j.processor.Processor;

@Processor(The.FILE_PROCESSOR)
public class FileProcessor {
	
	@Instruction(Instructions.LOAD_A_FILE)
	public String loadText(String path){
		return FileUtils.loadTextFromFile(path);
	}
	
}
