package clear4j.processor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class ProcessorLoader {
	public static final String DEFAULT_PROCESSOR_PACKAGE = "clear4j";
	
	private static final Map<String, Class> processors = new HashMap<String, Class>();
	
	static {
		
		try {
			for(Class loaded : CustomLoader.getClasses(DEFAULT_PROCESSOR_PACKAGE)){
//				if (loaded.getAnnotation(Processor.class) != null){
//					// set up available processors repository
//					processors.put(loaded.getName(), loaded);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final Map<String, Class> getAvailableProcessors(){
		return Collections.unmodifiableMap(processors);
	}
	
	
}
