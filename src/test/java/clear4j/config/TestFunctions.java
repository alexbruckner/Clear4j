package clear4j.config;

import clear4j.beans.Function;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.beans.HostPort;
import clear4j.processor.Arg;
import clear4j.processors.PrintProcessor;

public class TestFunctions {

	public static Function println(Arg... args) {
        return new Function(new HostPort(Host.LOCAL_HOST.getHost(), 7777), PrintProcessor.class, "println", Object.class, args);
    }

}
