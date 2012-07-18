package clear4j;

import java.io.Serializable;

public class Instruction  implements Serializable {

    final String operation;
    final Param[] params;

    public Instruction(String operation, Param... params){
        this.operation = operation;
        this.params = params;
    }

    public String getOperation() {
        return operation;
    }

    public Param[] getParams() {
        return params;
    }
}
