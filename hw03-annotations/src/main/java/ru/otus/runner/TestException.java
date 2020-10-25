package ru.otus.runner;

public final class TestException extends RuntimeException {

    private final String reason;

    public TestException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public String reason() {
        return reason;
    }
}
