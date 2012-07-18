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
import java.util.ArrayList;
import java.util.List;
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
        	
        	//TODO check this - only register local processors
        	if (processor.getHost().isLocal()){
        	
	            Messenger.register(new DefaultQueue(processor.name(), processor.getHost()), new MessageListener<Instruction>(){
	
	                @Override
	                public void onMessage(Message<Instruction> message) {
	                	
	                	Instruction instr = message.getPayload();
	                	
	                	String operation = instr.getOperation();
	
	                    try {
	                    	
	                    	Class<?> processorClass = processor.getProcessorClass();
	                    	//TODO new instance?
	                        Object processorObject = processorClass.getConstructor().newInstance();
	                        
	                        for (final Method method : processor.getProcessorClass().getDeclaredMethods()){
	                            
	                        	Process annotation = method.getAnnotation(Process.class);
	                            if (annotation != null && method.getName().equals(operation)){
	                                  
	                                List<Object> args = new ArrayList<Object>();
	                                  
	                                for (Annotation[] paramAnnotations : method.getParameterAnnotations()){
	                                	for (Annotation paramAnnotation : paramAnnotations) {
	                                		if (paramAnnotation instanceof Value){
	                                			args.add(instr.getValue(((Value) paramAnnotation).value()));
	                                		}
	                                	}
	                                }
	                                
	                                instr.setValue(String.format("%s.%s", processor.name(), operation), (Serializable) method.invoke(processorObject, args.toArray()));
	                                
	                                System.out.println(instr);
	                                
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

}
