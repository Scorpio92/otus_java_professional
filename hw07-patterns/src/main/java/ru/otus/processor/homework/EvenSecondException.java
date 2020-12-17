package ru.otus.processor.homework;

public class EvenSecondException extends RuntimeException {

    public EvenSecondException(long currentSecond) {
        super(String.format("Current second is [%s]", currentSecond));
    }
}
