package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryMessageStorage implements MessageStorage {

    private final List<HistoryElement> elements = new ArrayList<>();

    @Override
    public void save(Message oldMsg, Message newMsg) {
        elements.add(new HistoryElement(elements.size() + 1, oldMsg, newMsg));
    }

    @Override
    public Iterable<HistoryElement> elements() {
        return Collections.unmodifiableCollection(elements);
    }
}
