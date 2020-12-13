package ru.otus.base.exceptions;

import ru.otus.base.Denomination;

public class CellExistsException extends IllegalStateException {

    public CellExistsException(Denomination denomination) {
        super(String.format("Cell with denomination [%s] already registered in ATM", denomination));
    }
}
