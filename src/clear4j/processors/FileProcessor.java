package clear4j.processors;

import clear4j.processor.Return;
import clear4j.processor.Value;


public class FileProcessor {
	
	@Return("text") // puts result of process(message['path']) into message['text'].
	public String process(@Value("path") String path){
		return FileUtils.loadTextFromFile(path);
	}
	
}
