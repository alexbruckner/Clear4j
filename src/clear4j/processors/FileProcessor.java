package clear4j.processors;

import clear4j.processor.Process;
import clear4j.processor.Value;


public class FileProcessor {
	
	@Process // returns result of process(message['path']) into message['?'] (depending on instruction).
	public String process(@Value("path") String path){
		return FileUtils.loadTextFromFile(path);
	}
	
}
