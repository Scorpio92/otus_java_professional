package ru.otus.proxy;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ProxyTool {

    public static <T> T create(Object originalObject, Predicate<Method> filter, Consumer<InvokeInfo> argumentConsumer) {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(originalObject.getClass());
        factory.setFilter(filter::test);
        try {
            return (T) factory.create(new Class<?>[0], new Object[0], new MH(originalObject, argumentConsumer));
        } catch (Exception e) {
            throw new RuntimeException("failed to create class factory", e);
        }
    }

    private static class MH implements MethodHandler {

        private final Object object;
        private final Consumer<InvokeInfo> argumentConsumer;

        public MH(Object object, Consumer<InvokeInfo> argumentConsumer) {
            this.object = object;
            this.argumentConsumer = argumentConsumer;
        }

        @Override
        public Object invoke(Object self, Method method, Method proceed, Object[] args) throws Throwable {
            argumentConsumer.accept(new InvokeInfo(method, args));
            return method.invoke(object, args);
        }
    }
}
