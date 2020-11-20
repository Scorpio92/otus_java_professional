package ru.otus;

import static ru.otus.LogUtils.logFilter;
import static ru.otus.LogUtils.methodHandleAction;
import static ru.otus.proxy.ProxyTool.create;

public final class Ioc {

    private Ioc() {
    }

    public static SomeClass get() {
        return create(new SomeClass(), logFilter(), methodHandleAction());
    }
}
