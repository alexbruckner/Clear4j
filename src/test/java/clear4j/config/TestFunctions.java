package clear4j.config;

import clear4j.Function;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processors.PrintProcessor;

public class TestFunctions {

    public static Function println(){
        return new Function(PrintProcessor.class, new HostPort(Host.LOCAL_HOST.getHost(), 7777), "println");
    }

}
