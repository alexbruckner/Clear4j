package clear4j;

import clear4j.msg.Message;
import clear4j.msg.Messenger;
import clear4j.msg.Receiver;

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

    public static Processor instruct(The processor) {    //TODO interface here
        return new Processor(processor);
    }

    public static final class Processor implements clear4j.Processor{

        private final The processor;

        private Processor(The processor){
            this.processor = processor;
        }

        @Override
        public Workflow to(clear4j.Instruction doSomething, String value) {
            // TODO send a message to the in-queue for the processor.
            Messenger.send(doSomething.name(), value).to(processor.name());
            return null;
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
                            clear4j.processor.Instruction annotation = method.getAnnotation(clear4j.processor.Instruction.class);
                            if (annotation != null){
                                clear4j.Instruction instruction = annotation.value();
                                if (message.getMessage().equals(instruction.name())){

                                    Object result = method.invoke(processorObject, message.getValue()); //TODO args
                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!: result = " + result);
                                    //TODO CONTINUE HERE.
                                    //TODO add result to message and pass on to... somewhere.
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
