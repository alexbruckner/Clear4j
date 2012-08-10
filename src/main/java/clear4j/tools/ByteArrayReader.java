package clear4j.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Alex
 * Date: 10/08/12
 * Time: 18:30
 */
public class ByteArrayReader {

	public static void main(String[] args) throws IOException {

		FileInputStream fis = new FileInputStream(new File("/temp/img/favicon.ico"));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		System.out.println(Arrays.toString(buffer));

	}

}
