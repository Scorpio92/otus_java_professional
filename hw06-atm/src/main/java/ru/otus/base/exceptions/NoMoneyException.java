package ru.otus.base.exceptions;

public class NoMoneyException extends IllegalStateException {

    public NoMoneyException() {
        super("Atm don't have required amount");
    }
}
