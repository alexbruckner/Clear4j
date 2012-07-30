package clear4j.monitor.beans;

import clear4j.beans.Workflow;

import java.io.Serializable;
import java.util.List;

public class Results implements Serializable {

    private final String result;

    public Results (List<Workflow> list) {
        StringBuilder sb = new StringBuilder();
        for (Workflow workflow : list){
            sb.append(new Result(workflow));
        }
        sb.append(String.format("%n"));
        result = sb.toString();
    }

    @Override
    public String toString() {
        return result;
    }
}
