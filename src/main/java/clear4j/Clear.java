package clear4j;

import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.processor.Arg;
import clear4j.processor.CustomLoader;
import clear4j.processor.instruction.Instruction;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:44
 */
public final class Clear {
	
	private static final Set<String> definedOperations;

    private Clear() {
    }

    private static final Logger LOG = Logger.getLogger(Clear.class.getName());

    public static void run(Workflow workflow) {
        run(workflow, workflow.getNextInstruction());
    }

    private static void run(Workflow workflow, Serializable returnValue) {
        run(workflow, workflow.getNextInstruction(returnValue)); //TODO
    }

    private static void run(Workflow workflow, Instruction<?> instr){
    	if (instr != null) {
            Function function = instr.getFunction();
            Messenger.send(new DefaultQueue(function.getProcessorClass().getName(), function.getHost()), workflow);
        }
    }

    static {
    	definedOperations = new HashSet<String>();
    	init();
    }
    
    private static void init() {
    	//load all function definitions
    	try {
    		
    		String additionalConfigClassName = System.getProperty("clear4j.config.class"); 
    		if (additionalConfigClassName != null) {
    			Class<?> additionalConfigClass = Class.forName(additionalConfigClassName);
    			setupConfigClass(additionalConfigClass);
    		}
    		
			for(Class<?> loaded : CustomLoader.getClasses("clear4j")){ 
				if (loaded.getAnnotation(Config.class) != null){
					setupConfigClass(loaded);
				}
			}
			
			String customConfigPackage = System.getProperty("clear4j.config.package"); 
    		if (customConfigPackage != null) {
    			for(Class<?> loaded : CustomLoader.getClasses(customConfigPackage)){ 
    				if (loaded.getAnnotation(Config.class) != null){
    					setupConfigClass(loaded);
    				}
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
    }
    

	private static void setupConfigClass(Class<?> loaded) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (Method method : loaded.getDeclaredMethods()){
			if (Function.class == method.getReturnType()){
				Function function = (Function) method.invoke(null);
				setup(function);
			}
		}
	}
	
	

	private static void setup(Function function) {
		if (!definedOperations.contains(function.getOperation())) {
			
			definedOperations.add(function.getOperation());

            if (LOG.isLoggable(Level.INFO)){
                LOG.info(String.format("Adding function [%s]", function));
            }
			
	        if (function.getHost().isLocal()) {
	
	        	final Class<?> processorClass = function.getProcessorClass();
	        	
	            Messenger.register(new DefaultQueue(processorClass.getName(), function.getHost()), new MessageListener<Workflow>() {
	
	                @Override
	                public void onMessage(Message<Workflow> message) {
	
	                    Workflow workflow = message.getPayload();
	
	                    Instruction<?> instr = workflow.getCurrentInstruction();
	
	                    String operation = instr.getFunction().getOperation();
                        Arg<?>[] args = instr.getFunction().getArgs();

	                    try {
	
	                        //TODO new instance?
	                        Object processorObject = processorClass.getConstructor().newInstance();
	
	                        for (final Method method : processorClass.getDeclaredMethods()) {
	                            if (args.length > 0 && !(method.getParameterTypes().length > 1)){
                                    continue;   // only look for matching methods
                                }
	                        	clear4j.processor.Function annotation = method.getAnnotation(clear4j.processor.Function.class);
	                            if (annotation != null && method.getName().equals(operation)) {
	
	                            	if (processorClass != Functions.finalProcess().getProcessorClass()) {

                                        if (LOG.isLoggable(Level.INFO)){
                                            LOG.info(String.format("Invoking method [%s] on [%s] with piped value [%s] and args [%s]", method.getName(), processorObject, instr.getValue(), Arrays.toString(args)));
                                        }

	                                    Serializable returnValue;
	                            		if(args.length > 0){
	                                    	returnValue = (Serializable) method.invoke(processorObject, instr.getValue(), instr.getFunction().getArgs());
	                                    } else {
	                  	                   	returnValue = (Serializable) method.invoke(processorObject, instr.getValue());
	                                    }

                                        if (LOG.isLoggable(Level.INFO)){
                                            LOG.info(String.format("Returned value: [%s]", returnValue));
                                        }

	                                    run(workflow, returnValue);
	                                    
	                            	} else {
	                            		method.invoke(processorObject, workflow);
	                            	}
	
	                            }
	                        }
	
	                    } catch (Exception e) {
	                        LOG.log(Level.SEVERE, e.getMessage());
	                        e.printStackTrace();
	                    }
	                }
	            });
	
	        }
		}
	}
    
	public static void main(String[] args) {
		Clear.start();
	}

	public static void start() {
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}		
	}


}
