package clear4j.util;

import clear4j.beans.Function;
import clear4j.processor.instruction.Instruction;

import java.io.Serializable;
import java.lang.reflect.Method;

public final class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static boolean methodMatched(Function function) {
		return match(function) != null;
	}

	public static Method match(Function function) {
		Class<?> processorClass = function.getProcessorClass();
		String operation = function.getOperation();
		Class<?> operationType = function.getRuntimeArgumentType();

		Method method;
		try {
			if (operationType == null) {
				method = processorClass.getDeclaredMethod(operation);
			} else {
				method = processorClass.getDeclaredMethod(operation, operationType);
			}

		} catch (NoSuchMethodException e) {
			String msg = String.format("--> tried to get method (%s, %s, %s)%n", processorClass, operation, operationType);
			throw new RuntimeException(msg, e); //TODO
		}

		return method;
	}

	public static Serializable invoke(Instruction instr) {
		Function function = instr.getFunction();
		Method method = match(function);
		Class<?> processorClass = function.getProcessorClass();
		Serializable value = instr.getValue();
		try {
			Object processorObject = processorClass.getConstructor().newInstance();
			if (value != null) {
				return (Serializable) method.invoke(processorObject, value);
			} else {
				return (Serializable) method.invoke(processorObject);
			}
		} catch (Exception e) {
			String msg = String.format("--> tried to invoke method (%s, %s, %s)%n", processorClass, method.getName(), value);
			throw new RuntimeException(msg, e);
		}
	}


}
