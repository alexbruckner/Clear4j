package clear4j;

import clear4j.config.Config;
import clear4j.beans.Function;
import clear4j.beans.Workflow;
import clear4j.msg.Messenger;
import clear4j.msg.beans.DefaultQueue;
import clear4j.msg.queue.Host;
import clear4j.msg.queue.Message;
import clear4j.msg.queue.MessageListener;
import clear4j.processor.Arg;
import clear4j.processor.CustomLoader;
import clear4j.processor.instruction.Instruction;
import clear4j.web.WebServer;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: alexb
 * Date: 24/05/12
 * Time: 16:44
 */
public final class Clear {

	private static final Logger LOG = Logger.getLogger(Clear.class.getName());

	private static final Map<Class<?>, Set<Function>> DEFINED_FUNCTIONS;

	private Clear() {
	}

	public static Workflow run(Workflow workflow) {
		Runner.run(workflow, workflow.getNextInstruction());
		return workflow;
	}

	public static Workflow run(Function function) {
		Workflow workflow = new Workflow(function);
		return run(workflow);
	}

	static {
		System.out.println(String.format("running on port [%s]", Host.LOCAL_HOST.getPort()));
		DEFINED_FUNCTIONS = new ConcurrentHashMap<Class<?>, Set<Function>>();
		init();
	}

	private static void init() {
		//load all function definitions
		try {

			String additionalConfigClassName = System.getProperty("clear4j.config.class");
			if (additionalConfigClassName != null) {
				Class<?> additionalConfigClass = Class.forName(additionalConfigClassName);
				prepareConfigClass(additionalConfigClass);
			}

			for (Class<?> loaded : CustomLoader.getClasses("clear4j.config")) {
				if (loaded.getAnnotation(Config.class) != null) {
					prepareConfigClass(loaded);
				}
			}

			String customConfigPackage = System.getProperty("clear4j.config.package");
			if (customConfigPackage != null) {
				for (Class<?> loaded : CustomLoader.getClasses(customConfigPackage)) {
					if (loaded.getAnnotation(Config.class) != null) {
						prepareConfigClass(loaded);
					}
				}
			}

			setupProcessors();

			//webserver
			String webserverPort = System.getProperty("clear4j.monitor.port");
			if (webserverPort != null) {
				try {
					int port = Integer.parseInt(webserverPort);
					new WebServer(port);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	private static void prepareConfigClass(Class<?> loaded) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		for (Method method : loaded.getDeclaredMethods()) {
			if (Function.class == method.getReturnType()) {
				Function function = (Function) method.invoke(null);
				if (function.getHost().isLocal()) {  // only setup processors for local functions
					addToDefinedFunctions(function);
				}
			}
		}
	}

	private static void addToDefinedFunctions(Function function) {
		final Class<?> processorClass = function.getProcessorClass();

		Set<Function> definedFunctions = DEFINED_FUNCTIONS.get(processorClass);

		if (definedFunctions == null) {
			definedFunctions = new HashSet<Function>();
			DEFINED_FUNCTIONS.put(processorClass, definedFunctions);
		}

		definedFunctions.add(function);
	}


	private static void setupProcessors() {

		for (Map.Entry<Class<?>, Set<Function>> e : DEFINED_FUNCTIONS.entrySet()) {

			Class<?> processorClass = e.getKey();
			Set<Function> definedFunctions = e.getValue();

			if (LOG.isLoggable(Level.INFO)) {
				LOG.info(String.format("Setting up processor [%s]", processorClass));
				LOG.info(String.format(" -> with functions [%s]", definedFunctions));
			}

			setup(processorClass, definedFunctions);

		}

	}

	private static void setup(final Class<?> processorClass, final Set<Function> definedFunctions) {

		verify(processorClass, definedFunctions);

		Messenger.register(new DefaultQueue(processorClass.getName(), Host.LOCAL_HOST), new MessageListener<Workflow>() {

			@Override
			public void onMessage(Message<Workflow> message) {
				Workflow workflow = message.getPayload();
				processWorkflow(workflow);
			}

		});
	}

	private static void verify(Class<?> processorClass, Set<Function> definedFunctions) {

		for (Function definedFunction : definedFunctions) {

			String methodName = definedFunction.getOperation();
			Class<?> runtimeArgumentType = definedFunction.getRuntimeArgumentType();

			System.out.format("verifying %s.%s", processorClass.getName(), methodName);

			//the class can define multiple methods of the same name with different parameter types
			// which method gets called depends on whether the instruction has a value set //TODO rethink allowed method signatures
			//ie 1, void/Object println()  , ie no piped value set
			//ie 2, Object println(Object pipedValue)
			//ie 3, Object println(Object pipedValue, Arg<?>[] arguments) // TODO will replace arguments arrays with variable length annotated arguments, ie @Value("key")

			if ((runtimeArgumentType != null && isMethodDefined(processorClass, methodName, runtimeArgumentType, Arg[].class))
					|| (runtimeArgumentType != null && isMethodDefined(processorClass, methodName, runtimeArgumentType))
					|| (runtimeArgumentType == null && isMethodDefined(processorClass, methodName))) {

				System.out.println("verified.");

			} else {
				throw new RuntimeException(String.format("not verified: %s.%s", processorClass.getName(), methodName));
			}

		}

	}

	private static boolean isMethodDefined(Class<?> processorClass, String methodName, Class<?>... parameterTypes) {
		try {
			processorClass.getDeclaredMethod(methodName, parameterTypes);
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	private static void processWorkflow(Workflow workflow) {
		Instruction<?> instr = workflow.getCurrentInstruction();

		Class<?> processorClass = instr.getFunction().getProcessorClass();
		String operation = instr.getFunction().getOperation();
		Class<?> argumentType = instr.getFunction().getRuntimeArgumentType();
		Arg<?>[] args = instr.getFunction().getArgs();

		if (LOG.isLoggable(Level.INFO)) {
			LOG.info(String.format("operation [%s]", operation));
		}

		try { //TODO all this needs cleaning up!!!!!!!!!!!!!!

			//TODO new instance?
			Object processorObject = processorClass.getConstructor().newInstance();


			if (operation.equals("initialProcess") || operation.equals("finalProcess")) {
				Method method = processorClass.getMethod(operation, argumentType);
				method.invoke(processorObject, workflow);
				if (operation.equals("initialProcess")) {
					Clear.run(workflow);
				}
			} else {

				if (LOG.isLoggable(Level.INFO)) {
					LOG.info(String.format("Invoking method [%s] on [%s] with piped value [%s] and args [%s]", operation, processorObject, instr.getValue(), Arrays.toString(args)));
				}
				try {
					Serializable returnValue;
					if (args.length > 0) {
						Method method = processorClass.getMethod(operation, argumentType, Arg[].class);
						returnValue = (Serializable) method.invoke(processorObject, instr.getValue(), instr.getFunction().getArgs());
					} else if (argumentType != null) {
						Method method = processorClass.getMethod(operation, argumentType);
						returnValue = (Serializable) method.invoke(processorObject, instr.getValue());
					} else {
						Method method = processorClass.getMethod(operation);
						returnValue = (Serializable) method.invoke(processorObject);
					}
					if (LOG.isLoggable(Level.INFO)) {
						LOG.info(String.format("Returned value: [%s]", returnValue));
					}

					//run next instruction in the workflow piping in the return value returned from this call
					instr.setDone(true);
					Runner.run(workflow, returnValue);

				} catch (Exception e) {
					instr.setException(e);
					Runner.runFinalProcess(workflow);
					throw new RuntimeException(String.format("Exception in workflow [%s] at instruction [%s]", workflow, instr), e);
				}
			}

		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				Clear.start();
			}
		}).start();
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
