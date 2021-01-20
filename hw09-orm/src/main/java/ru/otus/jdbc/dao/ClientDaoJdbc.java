package ru.otus.jdbc.dao;

import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class ClientDaoJdbc implements ClientDao {

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<Client> jdbcMapper;

    public ClientDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<Client> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Client> findById(long id) {
        return jdbcMapper.findById(id, Client.class);
    }

    @Override
    public long insert(Client client) {
        return jdbcMapper.insert(client);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
