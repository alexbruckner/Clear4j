package clear4j.processors;


public class PrintProcessor {

	public void println() {
		System.out.println();
	}

	public Object println(Object value) {
		System.out.println(value);
		return value;
	}

}
