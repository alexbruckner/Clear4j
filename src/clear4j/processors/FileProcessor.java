package clear4j.processors;

import clear4j.processor.Key;
import clear4j.processor.Process;


public class FileProcessor {
	
	@Process("text") // puts result of process(message['path']) into message['text'].
	public String process(@Key("path") String path){ 
		return FileUtils.loadTextFromFile(path);
	}
	
}
