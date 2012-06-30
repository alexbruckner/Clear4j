package clear4j.processors;

import clear4j.processor.Key;
import clear4j.processor.Process;


public class FileProcessor {
	
	@Process
	public String process(@Key("path") String path){
		return FileUtils.loadTextFromFile(path);
	}
	
}
