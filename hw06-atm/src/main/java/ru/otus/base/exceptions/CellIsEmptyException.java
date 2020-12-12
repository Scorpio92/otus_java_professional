package ru.otus.base.exceptions;

import ru.otus.base.Denomination;

public class CellIsEmptyException extends IllegalStateException {

    public CellIsEmptyException(Denomination denomination) {
        super(String.format("Atm cell [%s] is empty", denomination));
    }
}
