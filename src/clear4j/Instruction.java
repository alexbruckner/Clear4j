package clear4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Instruction  implements Serializable {

	private static final long serialVersionUID = 1L;
	final String operation;
    final Map<String, Serializable> args;

    public Instruction(final String operation, final Param... params){
        this.operation = operation;
        this.args = new ConcurrentHashMap<String, Serializable>(params.length);
        for (Param param : params){
        	args.put(param.getKey(), param.getValue());
        }
    }

    public String getOperation() {
        return operation;
    }

    public <T extends Serializable> T getValue(String key) {
        return (T) args.get(key);
    }
    
    public <T extends Serializable> void setValue(String key, T value){
    	args.put(key, value);
    }
    
    @Override
    public String toString(){
    	return String.format("Instruction[op:%s,params:%s]", operation, args);
    }
}
