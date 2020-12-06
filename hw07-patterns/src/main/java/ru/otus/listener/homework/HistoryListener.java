package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener {

    private final IMessageStorage messageStorage;

    public HistoryListener(IMessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        messageStorage.save(oldMsg, newMsg);
    }
}
