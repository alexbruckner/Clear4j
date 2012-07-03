package clear4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import clear4j.msg.Message;

/**
 * User: alex
 * Date: 23/06/12
 * Time: 14:33
 */
public interface Instruction<T> {    
    void to(The processor);
	Instruction<T> toAndWait(The processor);
	Message<ConcurrentHashMap<String, Serializable>> get();
}
