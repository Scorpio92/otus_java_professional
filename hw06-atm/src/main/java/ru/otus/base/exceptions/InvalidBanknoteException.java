package ru.otus.base.exceptions;

import ru.otus.base.Banknote;

public class InvalidBanknoteException extends IllegalArgumentException {

    public InvalidBanknoteException(Banknote banknote, String cause) {
        super(String.format("Banknote [%s] is invalid. Cause: [%s]", banknote.toString(), cause));
    }
}
