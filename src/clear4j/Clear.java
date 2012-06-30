package clear4j;

import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.Receiver;
import clear4j.processor.Key;

import java.io.Serializable;
import java.lang.annotation.Annotation;
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
    	return new Instruction(key, value); //TODO on(remote).to()
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
                                  if (LOG.isLoggable(Level.INFO)){
                                	  LOG.log(Level.INFO, String.format("%s=%s", message.getMessage(), message.getPayload()));
                                  }
                                  //TODO redesign to allow for multiple annotations
                                  String key = null;
                                  for (Annotation[] paramAnnotations : method.getParameterAnnotations()){
                                	for (Annotation paramAnnotation : paramAnnotations) {
                                		if (paramAnnotation instanceof Key){
                                			key = ((Key) paramAnnotation).value();
                                		}
                                	}
                                  }
                                  
                                  if (key != null && message.getMessage().equals(key)){
                                      Object result = method.invoke(processorObject, message.getPayload()); //TODO args
                                      String resultKey = annotation.value();
                                      if (LOG.isLoggable(Level.INFO)){
                                    	  LOG.log(Level.INFO, String.format("%s=%s", resultKey, result));
                                      }
                                      // TODO put result into message and send on to somewhere...
                                      // if somewhere not specified, return message to ... finalProcessor.                       }
                                      // or put it into some workflow object?
                                      
                                      //TODO CONTINUE HERE.
                                  }
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
