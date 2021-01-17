package ru.otus.jdbc.dao;

import ru.otus.core.dao.AccountDao;
import ru.otus.core.model.Account;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {

    private final SessionManagerJdbc sessionManager;
    private final JdbcMapper<Account> jdbcMapper;

    public AccountDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<Account> jdbcMapper) {
        this.sessionManager = sessionManager;
        this.jdbcMapper = jdbcMapper;
    }

    @Override
    public Optional<Account> findById(String no) {
        return jdbcMapper.findById(no, Account.class);
    }

    @Override
    public long insertOrUpdate(Account account) {
        return jdbcMapper.insertOrUpdate(account);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
