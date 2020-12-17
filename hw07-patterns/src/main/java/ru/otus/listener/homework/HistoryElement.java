package ru.otus.listener.homework;

import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class HistoryElement {

    private final int id;
    private final Message old;
    private final Message actual;

    public HistoryElement(int id, Message old, Message actual) {
        this.id = id;
        this.old = copy(old);
        this.actual = copy(actual);
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

    private Message copy(Message source) {
        Message.Builder builder = source.toBuilder();
        if (source.getField13() != null && source.getField13().getData() != null) {
            List<String> data = new ArrayList<>(source.getField13().getData());
            ObjectForMessage ofm = new ObjectForMessage();
            ofm.setData(data);
            builder.field13(ofm);
        }
        return builder.build();
    }
}
