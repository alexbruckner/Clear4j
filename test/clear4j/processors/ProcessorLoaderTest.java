package clear4j.processors;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class ProcessorLoaderTest {

	@Test
	public void testCustomLoader() throws Exception {
		Class[] classes = CustomLoader.getClasses(ProcessorLoader.DEFAULT_PROCESSOR_PACKAGE);
		Assert.assertTrue(Arrays.asList(classes).contains(FileProcessor.class));
	}
	
	@Test
	public void testLoadedProcessors() {
		Assert.assertTrue(ProcessorLoader.getAvailableProcessors().containsValue(FileProcessor.class));
	}
	
}
