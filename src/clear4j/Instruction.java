package clear4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Instruction  implements Serializable {

	private static final long serialVersionUID = 1L;
	final Function function;
    final Map<String, Serializable> args;

    public Instruction(Function operation, final Param... values){
        this.function = operation;
        this.args = new ConcurrentHashMap<String, Serializable>(values.length);
        for (Param param : values){
        	args.put(param.getKey(), param.getValue());
        }
    }

    public Function getFunction() {
        return function;
    }

    public <T extends Serializable> T getValue(String key) {
        return (T) args.get(key);
    }
    
    public <T extends Serializable> void setValue(String key, T value){
    	args.put(key, value);
    }

    @Override
    public String toString() {
        return String.format("Instruction{function=%s, values=%s}", function, args);
    }
}
