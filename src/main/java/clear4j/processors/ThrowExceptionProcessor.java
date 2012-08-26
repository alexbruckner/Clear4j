package clear4j.processors;

import java.io.IOException;


public class ThrowExceptionProcessor {

	public void throwRuntimeException(){
		throw new RuntimeException("RuntimeException thrown in ThrowExceptionProcessor");
	}
	
	public void throwCheckedException() throws IOException {
		throw new IOException("IOException thrown in ThrowExceptionProcessor");
	}
}
