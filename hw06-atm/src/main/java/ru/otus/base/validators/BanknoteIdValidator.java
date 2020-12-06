package ru.otus.base.validators;

import ru.otus.base.Banknote;
import ru.otus.base.IBanknoteValidator;
import ru.otus.base.exceptions.InvalidBanknoteException;

import java.util.ArrayList;
import java.util.Collection;

public class BanknoteIdValidator implements IBanknoteValidator {

    private final Collection<Banknote> validatedBanknotes;

    public BanknoteIdValidator() {
        this.validatedBanknotes = new ArrayList<>();
    }

    @Override
    public void validate(Banknote banknote) throws InvalidBanknoteException {
        if (validatedBanknotes.contains(banknote)) {
            throw new InvalidBanknoteException(banknote, String.format("UID [%s] is duplicated", banknote.uid()));
        }
        validatedBanknotes.add(banknote);
    }
}
