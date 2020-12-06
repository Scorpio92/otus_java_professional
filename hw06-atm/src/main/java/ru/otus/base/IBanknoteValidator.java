package ru.otus.base;

import ru.otus.base.exceptions.InvalidBanknoteException;

/**
 * Валидатор банкнот
 * <p>
 * Вызывается при внесении в банкомат банкнот через купюроприёмник
 * <p>
 * Валидация выполняется по определённым правилам по имеющимся у банкноты свойствам
 */
public interface IBanknoteValidator {

    /**
     * Проверка банкноты на валидность
     *
     * @param banknote банкнота (купюра)
     * @throws InvalidBanknoteException исключение бросается в случае если банкнота не валидна
     */
    void validate(Banknote banknote) throws InvalidBanknoteException;
}
