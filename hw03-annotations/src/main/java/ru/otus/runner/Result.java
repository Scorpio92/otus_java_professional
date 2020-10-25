package ru.otus.runner;

import java.util.Optional;

public final class Result {

    private final Class<?> annotationType;
    private final boolean success;
    private final Throwable throwable;

    public Result(Class<?> annotationType, boolean success, Throwable throwable) {
        this.annotationType = annotationType;
        this.success = success;
        this.throwable = throwable;
    }

    public Class<?> annotationType() {
        return annotationType;
    }

    public boolean isSuccess() {
        return success;
    }

    public Optional<Throwable> error() {
        return Optional.ofNullable(throwable);
    }
}
