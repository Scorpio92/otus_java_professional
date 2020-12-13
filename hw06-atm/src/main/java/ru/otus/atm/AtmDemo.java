package ru.otus.atm;

import ru.otus.base.Banknote;
import ru.otus.base.BaseAtmCell;
import ru.otus.base.Denomination;
import ru.otus.base.IAtm;
import ru.otus.base.validators.BanknoteIdValidator;
import ru.otus.base.validators.DenominationValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class AtmDemo {

    public static void main(String... args) {
        IAtm atm = new Atm();

        atm.registerValidator(new BanknoteIdValidator());
        atm.registerValidator(new DenominationValidator(
                Denominations.RUB100,
                Denominations.RUB200,
                Denominations.RUB500,
                Denominations.RUB1000,
                Denominations.RUB5000)
        );

        atm.registerCell(new BaseAtmCell(Denominations.RUB100, 1000));
        atm.registerCell(new BaseAtmCell(Denominations.RUB500, 800));
        atm.registerCell(new BaseAtmCell(Denominations.RUB1000, 800));
        atm.registerCell(new BaseAtmCell(Denominations.RUB5000, 500));

        atm.putBanknotes(generateBanknotes(Denominations.RUB100, 100));
        atm.putBanknotes(generateBanknotes(Denominations.RUB500, 100));
        atm.putBanknotes(generateBanknotes(Denominations.RUB1000, 100));
        atm.putBanknotes(generateBanknotes(Denominations.RUB5000, 100));

        System.out.println("Get RUB banknotes: " + atm.getBanknotes(Currencies.RUB, 6600));

        System.out.println("Rest of RUB money in ATM: " + atm.getRestOfMoney().stream()
                .filter(banknote -> banknote.currency().equals(Currencies.RUB))
                .mapToInt(Banknote::denomination)
                .sum()
        );
    }

    private static Collection<Banknote> generateBanknotes(Denomination denomination, int count) {
        Collection<Banknote> banknotes = new ArrayList<>();
        for (int i = 0; i < count; i++) banknotes.add(new Banknote(UUID.randomUUID().toString(), denomination));
        return banknotes;
    }
}
