package ru.otus.listener.homework;

import ru.otus.model.Message;

public interface MessageStorage {

    void save(Message oldMsg, Message newMsg);

    Iterable<HistoryElement> elements();
}
