package clear4j.web.img;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: Alex
 * Date: 10/08/12
 * Time: 18:34
 */
public class Images {

	private static final Map<String, byte[]> DATA = new ConcurrentHashMap<String, byte[]>();

	public static byte[] get(String requestPath) throws IOException {
		byte[] data = DATA.get(requestPath);
		if (data == null) {
			data = load(requestPath);
			DATA.put(requestPath, data);
		}
		return data;
	}

	private static byte[] load(String requestPath) throws IOException {
		InputStream is = Images.class.getResourceAsStream(requestPath);
		byte[] data = new byte[is.available()];
		is.read(data);
		return data;
	}

}
