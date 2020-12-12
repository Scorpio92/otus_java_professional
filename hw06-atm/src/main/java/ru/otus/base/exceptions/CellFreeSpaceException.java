package ru.otus.base.exceptions;

import ru.otus.base.Denomination;

public class CellFreeSpaceException extends IllegalStateException {

    public CellFreeSpaceException(Denomination denomination, int banknotesCount) {
        super(String.format("Try to put %d banknotes in cell for denomination [%s]", banknotesCount, denomination));
    }
}
