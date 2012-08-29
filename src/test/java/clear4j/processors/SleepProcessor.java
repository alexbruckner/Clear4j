package clear4j.processors;


public class SleepProcessor {

	public Object sleep(Object value) throws InterruptedException {
		System.out.println("sleeping for 10 seconds.");
		Thread.sleep(10000);
		System.out.println("waking up.");
		return value;
	}

}
