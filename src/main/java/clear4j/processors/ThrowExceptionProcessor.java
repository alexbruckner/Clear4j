package clear4j.processors;

import java.io.IOException;

import clear4j.processor.Function;

public class ThrowExceptionProcessor {

	@Function
	public void throwRuntimeException(){
		throw new RuntimeException("RuntimeException thrown in ThrowExceptionProcessor");
	}
	
	@Function
	public void throwCheckedException() throws IOException {
		throw new IOException("IOException thrown in ThrowExceptionProcessor");
	}
}
