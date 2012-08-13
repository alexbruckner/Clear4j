package clear4j.processors;

import clear4j.processor.annotations.Function;


public class SleepProcessor {
	
	@Function
	public Object sleep(Object value) throws InterruptedException {
        System.out.println("sleeping for 10 seconds.");
        Thread.sleep(10000);
        System.out.println("waking up.");
        return value;
	}

}
