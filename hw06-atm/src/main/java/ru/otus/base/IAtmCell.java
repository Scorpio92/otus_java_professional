package ru.otus.base;

import ru.otus.base.exceptions.CellFreeSpaceException;
import ru.otus.base.exceptions.CellIsEmptyException;

import java.util.Collection;

/**
 * Интерфейс, описывающий работу с ячейкой банкомата
 * <p>
 * Ячейка банкомата может обрабатывать банкноты строго одного номинала
 * <p>
 * Каждая ячейка имеет ёмкость - максимальное кол-во банкнот, которое она может хранить
 */
public interface IAtmCell {

    /**
     * Проверить наличие свободного места в ячейке
     *
     * @param banknotesCount кол-во банкнот для которого осуществляется проверка
     * @return true, если место имеется
     */
    boolean checkFreeSpace(int banknotesCount);

    /**
     * Внести банкноты
     *
     * @param banknotes банкноты одного номинала
     * @throws CellFreeSpaceException заканчивается место для банкнот
     */
    void putBanknotes(Collection<Banknote> banknotes) throws CellFreeSpaceException;

    /**
     * Получить кол-во банкнот хранящихся в данный момент в ячейке
     *
     * @return кол-во банкнот
     */
    int banknotesCount();

    /**
     * Получить 1 банкноту
     *
     * @return банкнота
     * @throws CellIsEmptyException ячейка пуста
     */
    Banknote getBanknote() throws CellIsEmptyException;

    /**
     * Номинал с которым работает ячейка
     */
    Denomination denomination();
}
