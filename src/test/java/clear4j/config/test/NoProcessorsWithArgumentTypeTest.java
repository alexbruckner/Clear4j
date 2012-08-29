//package clear4j.config.test;
//
//import clear4j.Clear;
//import clear4j.beans.Function;
//import clear4j.processors.PrintProcessor;
//import junit.framework.Assert;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
///**
// * Created by IntelliJ IDEA.
// * User: Alex
// * Date: 29/08/12
// * Time: 13:43
// */
//public class NoProcessorsWithArgumentTypeTest {
//	@BeforeClass
//	public static void setup() {
//		System.setProperty("clear4j.config.class", "clear4j.config.test.NoProcessorsWithArgumentTypeTest");
//	}
//
//	public static Function nonExistentFunction() {
//		return new Function(PrintProcessor.class, "nonExistentFunction", String.class);
//	}
//
//	@Test
//	public void test() {
//		try {
//			Clear.start();
//		} catch (Throwable e) {
//			System.out.format("[%s]", e.getCause().getMessage());
//			Assert.assertEquals(String.format("java.lang.RuntimeException: Missing method in class clear4j.processors.PrintProcessor:  nonExistentFunction(class java.lang.String)%n"), e.getCause().getMessage());
//		}
//	}
//}
