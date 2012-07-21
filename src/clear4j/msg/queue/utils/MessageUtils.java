package clear4j.msg.queue.utils;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MessageUtils {
    private static final Logger LOG = Logger.getLogger(MessageUtils.class.getName());
    private MessageUtils(){}

    private static final int DEFAULT_PORT = 9876;     //TODO

    public static final String LOCAL_HOST = getLocalHost();

    public static final int LOCAL_PORT = getLocalPort();

    private static String getLocalHost(){
        try {
            return java.net.InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
        	e.printStackTrace();
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return "localhost";
    }

    private static int getLocalPort(){
        String property = System.getProperty("clear4j.port");
        if (property != null){
            try{
                return Integer.parseInt(property);
            } catch (NumberFormatException e){
            	e.printStackTrace();
                LOG.log(Level.WARNING, e.getMessage());
                return DEFAULT_PORT;
            }
        } else {
            return DEFAULT_PORT;
        }
    }
}
