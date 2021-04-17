package ru.otus.domain.dao;

import ru.otus.domain.model.Client;
import ru.otus.domain.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface ClientDao {

    Optional<Client> findByCredentials(String login, String password);

    List<Client> selectAll();

    long insert(Client client);

    SessionManager getSessionManager();
}
