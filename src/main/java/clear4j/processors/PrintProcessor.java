package clear4j.processors;


import clear4j.processor.Param;


public class PrintProcessor {

	public void println(){
		System.out.println();
	}
	
	public Object println(Object value){
        System.out.println(value);
        return value;
	}

    public Object println(Object value, Param<String> value1, Param<String> value2){
        System.out.println(String.format("Extra Args: value1 = [%s], value2 = [%s]", value1.getValue(), value2.getValue()));
        return println(value);
    }
}
