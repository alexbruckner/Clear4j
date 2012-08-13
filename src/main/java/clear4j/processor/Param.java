package clear4j.processor;

import java.io.Serializable;

public class Param<T extends Serializable> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String key;
	private final T value;
	
	public Param(final String key, final T value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("Param(%s=%s)", key, value);
	}
	
}
