package clear4j.processors;

import java.io.*;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:21
 */
public final class FileUtils {
	private FileUtils() {
	}

	public static String loadTextFromFile(String filePath) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			StringBuilder sb = new StringBuilder();
			while (bufferedReader.ready()) {
				sb.append(bufferedReader.readLine()).append("\n");
			}
			return sb.deleteCharAt(sb.length() - 1).toString();
		} catch (IOException ie) {
			ie.printStackTrace();
		} finally {
			close(bufferedReader);
		}
		return null;
	}

	public static void writeTextToFile(String filePath, String content) {
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(filePath));
			bufferedWriter.write(content);
			bufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(bufferedWriter);
		}
	}

	public static void removeFile(String filePath) {
		new File(filePath).deleteOnExit();
	}

	private static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
