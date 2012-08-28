package clear4j.util;

import clear4j.beans.Function;
import clear4j.processor.instruction.Instruction;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

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

		Class<?>[] parameterTypes;

		if (operationType == null) {
			parameterTypes = null;
		} else {
			parameterTypes = new Class<?>[]{operationType};
		}

		Method method = getDeclaredMethod(processorClass, operation, parameterTypes);

		System.out.format("isMethodDefined(%s, %s, %s)?: %s %n", processorClass, operation, Arrays.toString(parameterTypes), method != null);

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
				Object[] args = new Object[]{value};
				return (Serializable) method.invoke(processorObject, args);
			} else {
				return (Serializable) method.invoke(processorObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Method getDeclaredMethod(Class<?> processorClass, String methodName, Class<?>... parameterTypes) {
		try {
			return processorClass.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

}
