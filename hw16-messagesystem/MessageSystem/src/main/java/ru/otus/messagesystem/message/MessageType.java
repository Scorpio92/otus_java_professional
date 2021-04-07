package ru.otus.messagesystem.message;

public enum MessageType {
    ALL_USERS("AllUsers"),
    SAVE_USER("SaveUser");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
