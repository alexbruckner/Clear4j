package clear4j;

import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.processor.Process;
import clear4j.processor.Value;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:44
 */
public final class Clear {

	private Clear(){}

    private static final Logger LOG = Logger.getLogger(The.class.getName());

    public static void run(Instruction instruction, The processor) {
        Messenger.send(new DefaultQueue(processor.name(), processor.getHost()), instruction);
    }

    static {
        for (final The processor : The.values()){
            Messenger.register(new DefaultQueue(processor.name(), processor.getHost()), new MessageListener<String>(){

                @Override
                public void onMessage(Message message) {
                    //TODO call method of processor with message keys required
                    //TODO add return value to message;
                    //TODO send to result queue.
                	
                	Map<String, Serializable> map = (Map<String, Serializable>) message.getPayload();
                	
                    try {
                        Object processorObject = processor.getProcessorClass().getConstructor().newInstance();
                        for (final Method method : processor.getProcessorClass().getDeclaredMethods()){
                            Process annotation = method.getAnnotation(Process.class);
                            if (annotation != null){
                                  if (LOG.isLoggable(Level.INFO)){
                                	  LOG.log(Level.INFO, String.format("payload=%s", message.getPayload()));
                                  }
                                  //TODO redesign to allow for multiple annotations
                                  String key = null;
                                  for (Annotation[] paramAnnotations : method.getParameterAnnotations()){
                                	for (Annotation paramAnnotation : paramAnnotations) {
                                		if (paramAnnotation instanceof Value){
                                			key = ((Value) paramAnnotation).value();
                                		}
                                	}
                                  }
                                  
                                  if (key != null && (map).get(key)!=null){
                                      Serializable result = (Serializable) method.invoke(processorObject, map.get(key)); //TODO args
                                      String resultKey = processor.getProcessorClass().getName(); 
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
