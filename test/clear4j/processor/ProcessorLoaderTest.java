package clear4j.processor;

import clear4j.The;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ProcessorLoaderTest {

	@Test
	public void testCustomLoader() throws Exception {
		Class<?>[] classes = CustomLoader.getClasses(ProcessorLoader.DEFAULT_PROCESSOR_PACKAGE);
		Assert.assertTrue(Arrays.asList(classes).contains(The.class));
	}
	
	@Test
	public void testLoadedProcessors() {
		Assert.assertTrue(ProcessorLoader.getAvailableProcessors().containsValue(The.class));
	}
	
}
