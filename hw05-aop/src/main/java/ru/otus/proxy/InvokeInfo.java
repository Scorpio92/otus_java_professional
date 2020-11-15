package ru.otus.proxy;

import java.lang.reflect.Method;

public final class InvokeInfo {

    private final Method method;
    private final Object[] arguments;

    public InvokeInfo(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    public String methodName() {
        return method.getName();
    }

    public Object[] arguments() {
        return arguments;
    }
}
