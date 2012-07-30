package clear4j;

import clear4j.config.Config;
import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.processor.CustomLoader;
import clear4j.processors.WorkflowProcessor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private static final Set<Class<?>> definedProcessors;

    private Clear() {
    }

    private static final Logger LOG = Logger.getLogger(Clear.class.getName());

    public static Workflow run(Workflow workflow) {
        Runner.run(workflow, workflow.getNextInstruction());
        return workflow;
    }

    static {
    	System.out.println(String.format("running on port [%s]", Host.LOCAL_HOST.getPort()));
        definedOperations = new HashSet<String>();
        definedProcessors = new HashSet<Class<?>>();
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

            for (Class<?> loaded : CustomLoader.getClasses("clear4j.config")) {
                if (loaded.getAnnotation(Config.class) != null) {
                    setupConfigClass(loaded);
                }
            }

            String customConfigPackage = System.getProperty("clear4j.config.package");
            if (customConfigPackage != null) {
                for (Class<?> loaded : CustomLoader.getClasses(customConfigPackage)) {
                    if (loaded.getAnnotation(Config.class) != null) {
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
        for (Method method : loaded.getDeclaredMethods()) {
            if (Function.class == method.getReturnType()) {
                Function function = (Function) method.invoke(null);
                setup(function);
            }
        }
    }


    private static void setup(Function function) {
        if (!definedOperations.contains(function.getOperation())) {

            definedOperations.add(function.getOperation());

            if (LOG.isLoggable(Level.INFO)) {
                LOG.info(String.format("Adding function [%s]", function));
            }

            final Class<?> processorClass = function.getProcessorClass();

            if (function.getHost().isLocal() && !definedProcessors.contains(processorClass)) {

                definedProcessors.add(processorClass);

                Messenger.register(new DefaultQueue(processorClass.getName(), function.getHost()), new MessageListener<Workflow>() {

                    @Override
                    public void onMessage(Message<Workflow> message) {
                        Workflow workflow = message.getPayload();
                        WorkflowProcessor.processWorkflow(workflow);
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
