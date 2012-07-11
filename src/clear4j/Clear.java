package clear4j;

import clear4j.msg.Messenger;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.processor.Key;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:44
 */
public final class Clear {

	private Clear(){}

    public static clear4j.Instruction send(final String key, final Serializable value) {    //TODO interface here
    	return new Instruction(key, value); //TODO on(remote).to()
    }
    
    private static final class Instruction implements clear4j.Instruction{
    	private final ConcurrentHashMap<String, Serializable> values = new ConcurrentHashMap<String, Serializable>();
    	private Future<clear4j.msg.queue.Message<ConcurrentHashMap<String, Serializable>>> trackedMessage;
    	private Instruction (String key, Serializable value){
    		values.put(key, value);
    	}

		@Override
		public void to(The processor) { //TODO check this
			//Messenger.send(values).to(processor.name());//TODO
		}
		
		@Override
		public Instruction toAndWait(The processor) { //TODO check this
//			Message msg = Messenger.send(values);  //TODO
//			this.trackedMessage = Messenger.track(msg.getId(), The.FINAL_PROCESSOR.name());
//			msg.to(processor.name());
			return this;
		}
	
		@Override
		public Message<ConcurrentHashMap<String, Serializable>> get() {
			try {
				return trackedMessage.get();
			} catch (Exception e) {
				throw new RuntimeException(e); //TODO better handling
			}
		}

    }

    private static final Logger LOG = Logger.getLogger(The.class.getName());

    static {
        //TODO create (local only) listeners to in-queues for all defined processors
        //TODO which will then call the relevant method required.

        for (final The processor : The.values()){
            Messenger.register(processor.name(), new MessageListener<String>(){

                @Override
                public void onMessage(Message message) {
                    //TODO call method of processor with message keys required
                    //TODO add return value to message;
                    //TODO send to result queue.
                	
                	Map<String, Serializable> map = (Map<String, Serializable>) message.getPayload();
                	
                    try {
                        Object processorObject = processor.getProcessorClass().getConstructor().newInstance();
                        for (final Method method : processor.getProcessorClass().getDeclaredMethods()){
                            clear4j.processor.Process annotation = method.getAnnotation(clear4j.processor.Process.class);
                            if (annotation != null){
                                  if (LOG.isLoggable(Level.INFO)){
                                	  LOG.log(Level.INFO, String.format("payload=%s", message.getPayload()));
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
                                  
                                  if (key != null && (map).get(key)!=null){
                                      Serializable result = (Serializable) method.invoke(processorObject, map.get(key)); //TODO args
                                      String resultKey = annotation.value();
                                      if (LOG.isLoggable(Level.INFO)){
                                    	  LOG.log(Level.INFO, String.format("%s=%s", resultKey, result));
                                      }
                                      // TODO put result into message and send on to somewhere...
                                      // if somewhere not specified, return message to ... finalProcessor.                       }
                                      // or put it into some workflow object?
                                      map.put(resultKey, result);
                                      //TODO CONTINUE HERE.
                                      String nextProcessor = (String) map.get("next");
                                      if (nextProcessor == null) {
                                    	  nextProcessor = The.FINAL_PROCESSOR.name();
                                      }
                                      Messenger.send(nextProcessor, message.getPayload());
                                      //TODO now can wait on receiving on final processor!!!!!!
                                  }
                            }
                        }

                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, e.getMessage());
                        throw new RuntimeException(e);
                    }
                }

            });
        }
    }

}
