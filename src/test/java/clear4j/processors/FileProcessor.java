package clear4j.processors;


public class FileProcessor {

	public String loadText(String path) {
		return FileUtils.loadTextFromFile(path);
	}

	public String loadReversedText(String path) {
		return new StringBuilder().append(FileUtils.loadTextFromFile(path)).reverse().toString();
	}

}
