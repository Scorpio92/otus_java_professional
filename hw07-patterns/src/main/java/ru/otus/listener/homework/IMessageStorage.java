package ru.otus.listener.homework;

import ru.otus.model.Message;

import java.util.Collection;

public interface IMessageStorage {

    void save(Message oldMsg, Message newMsg);

    Collection<HistoryElement> elements();
}
