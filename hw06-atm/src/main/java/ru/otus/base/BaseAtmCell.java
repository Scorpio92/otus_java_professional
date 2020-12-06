package ru.otus.base;

import ru.otus.base.exceptions.CellFreeSpaceException;
import ru.otus.base.exceptions.CellIsEmptyException;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Objects;
import java.util.Queue;

/**
 * Базовая сущность ячейки банкомата для приёма любых банкнот, любого номинала
 * <p>
 * Например: RUB100, USD1000 и т.д.
 */
public class BaseAtmCell implements IAtmCell {

    /**
     * Номинал с которым работает ячейка
     */
    private final Denomination denomination;
    /**
     * Ёмкость ячейки
     */
    private final int capacity;
    /**
     * Коллекция хранящихся банкнот
     */
    private final Queue<Banknote> banknotes;

    public BaseAtmCell(Denomination denomination, int capacity) {
        this.denomination = denomination;
        this.capacity = capacity;
        this.banknotes = new ArrayDeque<>(capacity);
    }

    @Override
    public boolean checkFreeSpace(int banknotesCount) {
        return capacity - banknotesCount() >= banknotesCount;
    }

    @Override
    public void putBanknotes(Collection<Banknote> banknotes) throws CellFreeSpaceException {
        if (!checkFreeSpace(banknotes.size())) throw new CellFreeSpaceException(denomination, banknotes.size());
        this.banknotes.addAll(banknotes);
    }

    @Override
    public int banknotesCount() {
        return banknotes.size();
    }

    @Override
    public Banknote getBanknote() throws CellIsEmptyException {
        if (banknotes.isEmpty()) throw new CellIsEmptyException(denomination);
        return this.banknotes.remove();
    }

    @Override
    public Denomination denomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseAtmCell that = (BaseAtmCell) o;
        return denomination == that.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }

    @Override
    public String toString() {
        return "BaseAtmCell{" +
                "denomination=" + denomination +
                ", capacity=" + capacity +
                ", banknotes=" + banknotes +
                '}';
    }
}
