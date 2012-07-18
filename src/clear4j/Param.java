package clear4j;

import java.io.Serializable;

public class Param implements Serializable {
    final String key;
    final String value;

    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
