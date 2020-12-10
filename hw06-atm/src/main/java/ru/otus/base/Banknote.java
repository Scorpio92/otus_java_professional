package ru.otus.base;

import java.util.Objects;

/**
 * Сущность банкноты банка любого государства
 */
public final class Banknote {

    /**
     * Уникальный код который присваивается ЦБ при выпуске банкноты
     */
    private final String uid;
    /**
     * Номинал
     */
    private final Denomination denomination;

    public Banknote(String uid, Denomination denomination) {
        this.uid = uid;
        this.denomination = denomination;
    }

    public String uid() {
        return uid;
    }

    public int denomination() {
        return denomination.value();
    }

    public String currency() {
        return denomination.currency();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return uid.equals(banknote.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "uid='" + uid + '\'' +
                ", denomination=" + denomination +
                '}';
    }
}
