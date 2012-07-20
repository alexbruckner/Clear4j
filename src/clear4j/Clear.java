package clear4j;

import clear4j.processor.instruction.Instruction;
import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.processor.Process;

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

    private Clear() {
    }

    private static final Logger LOG = Logger.getLogger(The.class.getName());

    public static void run(Workflow workflow) {
        run(workflow, workflow.getNextInstruction());
    }

    private static void run(Workflow workflow, Serializable returnValue) {
        run(workflow, workflow.getNextInstruction(returnValue)); //TODO
    }

    private static void run(Workflow workflow, Instruction<?> instr){
    	if (instr != null) {
            The processor = instr.getFunction().getProcessor();
            Messenger.send(new DefaultQueue(processor.name(), processor.getHost()), workflow);
        }
    }

    static {
        for (final The processor : The.values()) {

            //TODO check this - only register local processors
            if (processor.getHost().isLocal()) {

                Messenger.register(new DefaultQueue(processor.name(), processor.getHost()), new MessageListener<Workflow>() {

                    @Override
                    public void onMessage(Message<Workflow> message) {

                        Workflow workflow = message.getPayload();

                        Instruction<?> instr = workflow.getCurrentInstruction();

                        String operation = instr.getFunction().getOperation();

                        try {

                            Class<?> processorClass = processor.getProcessorClass();
                            //TODO new instance?
                            Object processorObject = processorClass.getConstructor().newInstance();

                            for (final Method method : processor.getProcessorClass().getDeclaredMethods()) {

                                Process annotation = method.getAnnotation(Process.class);
                                if (annotation != null && method.getName().equals(operation)) {

                                	if (processor != The.FINAL_PROCESSOR) {
	                                    Serializable returnValue = (Serializable) method.invoke(processorObject, instr.getValue());
	                                    //this just stores the values for debugging
	                                    workflow.setValue(String.format("%s.%s(%s)", processor.name(), operation, instr.getValue()), returnValue);
	
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


}
