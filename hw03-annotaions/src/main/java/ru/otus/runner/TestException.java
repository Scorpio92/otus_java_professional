package main.java.ru.otus.runner;

public final class TestException extends RuntimeException {

    private final String reason;

    public TestException(String reason) {
        this.reason = reason;
    }

    public String reason() {
        return reason;
    }
}
