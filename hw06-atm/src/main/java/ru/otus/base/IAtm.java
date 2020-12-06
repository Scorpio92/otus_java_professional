package ru.otus.base;

import ru.otus.base.exceptions.*;

import java.util.Collection;

/**
 * Интерфейс, описывающий API банкомата
 */
public interface IAtm {

    /**
     * Зарегистрировать ячейку для хранения банкнот нужного номинала
     *
     * @param cell ячейка в которой хранятся банкноты строго определённого номинала
     * @throws CellExistsException ячейка уже зарегистрирована в банкомате
     */
    void registerCell(IAtmCell cell) throws CellExistsException;

    /**
     * Зарегистрировать валидатор банкнот
     *
     * @param validator валидатор банкнот
     */
    void registerValidator(IBanknoteValidator validator);

    /**
     * Внести банкноты/совершить оплату
     * <p>
     * Должно происходить пополнение нужных ячеек, в зав-ти от номинала банкноты
     *
     * @param banknotes банкноты, номинал может быть разный
     * @throws InvalidBanknoteException купюра не может быть обработана банкоматом, т.к.
     *                                  номинал не поддерживается данным банкоматом или не прошла проверку на подлинность
     * @throws CellFreeSpaceException   в одной из ячеек заканчивается место для банкнот, при совершении пополнения,
     *                                  ячейка может быть переполнена
     */
    void putBanknotes(Collection<Banknote> banknotes) throws InvalidBanknoteException, CellFreeSpaceException;

    /**
     * Запросить выдачу указанной суммы
     *
     * @param currency код валюты
     * @param amount   сумма
     * @return банкноты, номинал может быть разный
     * @throws NoMoneyException исключение будет брошено в случае отсутствия необходимой суммы
     */
    Collection<Banknote> getBanknotes(String currency, int amount) throws NoMoneyException;

    /**
     * Получить остаток денежных средств в банкомате из всех ячеек
     *
     * @return банкноты, номинал может быть разный
     * @throws AtmIsEmptyException исключение будет брошено в случае, если банкнот в банкомате не останется
     */
    Collection<Banknote> getRestOfMoney() throws AtmIsEmptyException;
}
