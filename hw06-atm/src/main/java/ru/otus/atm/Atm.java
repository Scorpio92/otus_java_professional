package ru.otus.atm;

import ru.otus.base.*;
import ru.otus.base.exceptions.*;

import java.util.*;
import java.util.stream.Collectors;

public class Atm implements IAtm {

    private final Map<Denomination, IAtmCell> cells;
    private final Collection<IBanknoteValidator> banknoteValidators;

    public Atm() {
        this.cells = new TreeMap<>(Comparator.comparing(Denomination::currency).thenComparing((o1, o2) -> Integer.compare(o2.value(), o1.value())));
        this.banknoteValidators = new ArrayList<>();
    }

    @Override
    public void registerCell(IAtmCell cell) throws CellExistsException {
        Denomination denomination = cell.denomination();
        if (cells.containsKey(denomination)) throw new CellExistsException(denomination);
        cells.put(denomination, cell);
    }

    @Override
    public void registerValidator(IBanknoteValidator validator) {
        banknoteValidators.add(validator);
    }

    @Override
    public void putBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteException, CellFreeSpaceException {
        validateBanknotes(banknotes);
        Map<Denomination, Collection<Banknote>> map = new HashMap<>();
        cells.keySet().forEach(denomination -> map.put(denomination, filterBanknotesByDenomination(denomination, banknotes)));
        map.forEach(this::throwIfNotFreeSpaceInCell);
        map.forEach(this::putBanknotesInCell);
    }

    @Override
    public Collection<Banknote> getBanknotes(String currency, int amount) throws NoMoneyException {
        if (amount > calcGeneralAmount()) throw new NoMoneyException();
        Collection<Banknote> banknotes = new ArrayList<>();

        int currentAmount = amount;
        for (Map.Entry<Denomination, IAtmCell> entry : cells.entrySet()) {
            Denomination d = entry.getKey();
            if (d.currency().equals(currency)) {
                int banknotesCount = currentAmount / d.value();
                banknotes.addAll(banknotesFromCell(entry.getValue(), banknotesCount));
                currentAmount = currentAmount - banknotesCount * d.value();
            }
        }

        return banknotes;
    }

    @Override
    public Collection<Banknote> getRestOfMoney() throws AtmIsEmptyException {
        throwIfAtmIsEmpty();
        Collection<Banknote> banknotes = new ArrayList<>();
        cells.forEach((denomination, cell) -> banknotes.addAll(banknotesFromCell(cell, cell.banknotesCount())));
        return banknotes;
    }

    private void validateBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteException {
        banknotes.forEach(banknote -> banknoteValidators.forEach(validator -> validator.validate(banknote)));
    }

    private Collection<Banknote> filterBanknotesByDenomination(Denomination denomination, Collection<Banknote> banknotes) {
        return banknotes.stream()
                .filter(banknote -> banknote.currency().equals(denomination.currency()))
                .filter(banknote -> banknote.denomination() == denomination.value())
                .collect(Collectors.toList());
    }

    private IAtmCell cellByDenomination(Denomination denomination) {
        return cells.get(denomination);
    }

    private void throwIfNotFreeSpaceInCell(Denomination denomination, Collection<Banknote> banknotes) throws CellFreeSpaceException {
        if (!cellByDenomination(denomination).checkFreeSpace(banknotes.size())) {
            throw new CellFreeSpaceException(denomination, banknotes.size());
        }
    }

    private void throwIfAtmIsEmpty() {
        if (cells.values().stream()
                .mapToInt(IAtmCell::banknotesCount)
                .sum() == 0) {
            throw new AtmIsEmptyException();
        }
    }

    private void putBanknotesInCell(Denomination denomination, Collection<Banknote> banknotes) {
        cellByDenomination(denomination).putBanknotes(banknotes);
    }

    private int calcGeneralAmount() {
        return cells.keySet()
                .stream()
                .mapToInt(this::calcAmountInCell)
                .sum();
    }

    private int calcAmountInCell(Denomination denomination) {
        return cellByDenomination(denomination).banknotesCount() * denomination.value();
    }

    private Collection<Banknote> banknotesFromCell(IAtmCell cell, int count) {
        Collection<Banknote> banknotes = new ArrayList<>();
        for (int i = 0; i < count; i++) banknotes.add(cell.getBanknote());
        return banknotes;
    }
}
