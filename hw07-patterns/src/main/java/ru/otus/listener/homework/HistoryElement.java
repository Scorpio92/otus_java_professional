package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.Objects;

public final class HistoryElement {

    private final int id;
    private final Message old;
    private final Message actual;

    public HistoryElement(int id, Message old, Message actual) {
        this.id = id;
        this.old = old.toBuilder().build();
        this.actual = actual.toBuilder().build();
    }

    public int id() {
        return id;
    }

    public Message old() {
        return old;
    }

    public Message actual() {
        return actual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryElement historyElement = (HistoryElement) o;
        return id == historyElement.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HistoryElement{" +
                "id=" + id +
                ", old=" + old +
                ", actual=" + actual +
                '}';
    }
}
