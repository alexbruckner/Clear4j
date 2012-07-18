package clear4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Instruction  implements Serializable {

	private static final long serialVersionUID = 1L;
	final String operation;
    final Map<String, Serializable> params;

    public Instruction(final String operation, final Param... params){
        this.operation = operation;
        this.params = new ConcurrentHashMap<String, Serializable>(params.length);
    }

    public String getOperation() {
        return operation;
    }

    public <T extends Serializable> T getValue(String key) {
        return (T) params.get(key);
    }
}
