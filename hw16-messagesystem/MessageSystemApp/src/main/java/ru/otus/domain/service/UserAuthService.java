package ru.otus.domain.service;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
