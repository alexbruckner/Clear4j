package clear4j.util;

import clear4j.beans.Function;
import clear4j.processor.Param;
import clear4j.processor.instruction.Instruction;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        Param<?>[] params = function.getParams();


        Class<?>[] parameterTypes;

        if (operationType == null){
            parameterTypes = null;
        } else {
            parameterTypes = new Class<?>[]{operationType};
        }
        if (params.length > 0) {
            Class<?>[] paramParameterTypes = createParamTypes(params);
            parameterTypes = CollectionUtils.concat(parameterTypes, paramParameterTypes);
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
        Param<?>[] params = function.getParams();
        try{
            Object processorObject = processorClass.getConstructor().newInstance();
            if (params.length == 0){
                if (value == null){
                    return (Serializable) method.invoke(processorObject);
                } else {
                    return (Serializable) method.invoke(processorObject, value);
                }
            } else {
                Object[] args = new Object[]{value};
                args = CollectionUtils.concat(args, params);
                return (Serializable) method.invoke(processorObject, args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?>[] createParamTypes(Param<?>[] params) {

        List<Class<?>> parameterTypes = new ArrayList<Class<?>>(params.length);

        for (Param param : params){
            parameterTypes.add(Param.class);
        }

        return parameterTypes.toArray(new Class<?>[parameterTypes.size()]);
    }

    private static Method getDeclaredMethod(Class<?> processorClass, String methodName, Class<?>... parameterTypes) {
        try {
            return processorClass.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

}
