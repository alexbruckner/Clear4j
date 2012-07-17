package clear4j.processors;

import clear4j.processor.Process;
import clear4j.processor.Value;


public class FileProcessor {
	
	@Process // returns result of process(message['path']) into message['FILE_PROCESSER.loadText'] (depending on instruction).
	public String loadText(@Value("path") String path){
		return FileUtils.loadTextFromFile(path);
	}
	
	@Process // returns result of process(message['path']) into message['FILE_PROCESSOR.loadReversedText'] (depending on instruction).
	public String loadReversedText(@Value("path") String path){
		return new StringBuilder().append(FileUtils.loadTextFromFile(path)).reverse().toString();
	}
	
}
