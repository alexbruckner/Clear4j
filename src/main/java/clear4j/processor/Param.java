package clear4j.processor;

import java.io.Serializable;

public class Param<T extends Serializable> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final T value;
	
	public Param(final T value){
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("Param(value=%s)", value);
	}
	
}
