package ru.otus.base.exceptions;

public class AtmIsEmptyException extends IllegalStateException {

    public AtmIsEmptyException() {
        super("Atm is empty");
    }
}
