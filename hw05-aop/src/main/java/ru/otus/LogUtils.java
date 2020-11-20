package ru.otus;

import ru.otus.proxy.InvokeInfo;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LogUtils {

    public static Predicate<Method> logFilter() {
        return method -> Arrays.stream(method.getDeclaredAnnotations()).anyMatch(a -> a.annotationType() == Log.class);
    }

    public static Consumer<InvokeInfo> methodHandleAction() {
        return invokeInfo -> {
            if (invokeInfo.arguments().length > 0) {
                StringBuilder argBuilder = new StringBuilder();
                Arrays.stream(invokeInfo.arguments()).forEach(o -> argBuilder.append(o).append(", "));
                argBuilder.delete(argBuilder.length() - 2, argBuilder.length() - 1);
                System.out.printf("Execute method [%s] with arguments [%s]\n", invokeInfo.methodName(), argBuilder.toString().trim());
            } else {
                System.out.printf("Execute method [%s] without arguments\n", invokeInfo.methodName());
            }
        };
    }
}
