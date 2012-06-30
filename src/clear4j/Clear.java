package clear4j;

import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.Receiver;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:44
 */
public final class Clear {

	private Clear(){}

    public static clear4j.Instruction send(String key, Serializable value) {    //TODO interface here
    	return new Instruction(key, value);
    }
    
    private static final class Instruction implements clear4j.Instruction{
    	private final String key;
    	private final Serializable value;
    	
    	private Instruction(String key, Serializable value){
    		this.key = key;
    		this.value = value;
    	}

		@Override
		public void to(The processor) { //TODO check this
			Message m = Messenger.send(key, value);
			m.to(processor.name()); //TODO message
		}
    }

    private static final Logger LOG = Logger.getLogger(The.class.getName());

    static {
        //TODO create (local only) listeners to in-queues for all defined processors
        //TODO which will then call the relevant method required.

        for (final The processor : The.values()){
            Messenger.register(new Receiver(){

                @Override
                public void onMessage(Message message) {
                    //TODO call method of processor with message keys required
                    //TODO add return value to message;
                    //TODO send to result queue.
                    try {
                        Object processorObject = processor.getProcessorClass().getConstructor().newInstance();
                        for (final Method method : processor.getProcessorClass().getDeclaredMethods()){
                            clear4j.processor.Process annotation = method.getAnnotation(clear4j.processor.Process.class);
                            if (annotation != null){
                                  System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!: key = " + message.getMessage() + ", value = " + message.getPayload());

//                                    Object result = method.invoke(processorObject, message.getValue()); //TODO args
//                                    //TODO CONTINUE HERE.
//                                    //TODO add result to message and pass on to... somewhere.
                            }
                        }

                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, e.getMessage());
                        throw new RuntimeException(e);
                    }
                }

            }).to(processor.name());
        }
    }

}
