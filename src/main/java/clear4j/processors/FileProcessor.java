package clear4j.processors;

import clear4j.processor.Function;


public class FileProcessor {
	
	@Function // returns result of process(message['path']) into message['FILE_PROCESSER.loadText'] (depending on instruction).
	public String loadText(String path){
		return FileUtils.loadTextFromFile(path);
	}
	
	@Function // returns result of process(message['path']) into message['FILE_PROCESSOR.loadReversedText'] (depending on instruction).
	public String loadReversedText(String path){
		return new StringBuilder().append(FileUtils.loadTextFromFile(path)).reverse().toString();
	}
	
}
