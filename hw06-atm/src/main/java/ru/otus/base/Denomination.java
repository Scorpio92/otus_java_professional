package ru.otus.base;

import java.util.Objects;

/**
 * Номинал валюты
 */
public final class Denomination {

    /**
     * Код валюты
     */
    private final String currency;
    /**
     * Численный номинал валюты
     */
    private final int value;

    public Denomination(String currency, int value) {
        this.currency = currency;
        this.value = value;
    }

    public String currency() {
        return currency;
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Denomination that = (Denomination) o;
        return value == that.value &&
                currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, value);
    }

    @Override
    public String toString() {
        return "Denomination{" +
                "currency='" + currency + '\'' +
                ", value=" + value +
                '}';
    }
}
