package ru.otus.base.validators;

import ru.otus.base.Banknote;
import ru.otus.base.Denomination;
import ru.otus.base.IBanknoteValidator;
import ru.otus.base.exceptions.InvalidBanknoteException;

import java.util.Set;

public class DenominationValidator implements IBanknoteValidator {

    private final Set<Denomination> allowedDenominations;

    public DenominationValidator(Denomination... denominations) {
        this.allowedDenominations = Set.of(denominations);
    }

    @Override
    public void validate(Banknote banknote) {
        Denomination denomination = new Denomination(banknote.currency(), banknote.denomination());
        if (!allowedDenominations.contains(denomination)) {
            throw new InvalidBanknoteException(banknote, String.format("Denomination [%s] is not supported", denomination));
        }
    }
}
