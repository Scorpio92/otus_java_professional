package ru.otus.data.dao;


import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.data.sessionmanager.DatabaseSessionHibernate;
import ru.otus.data.sessionmanager.SessionManagerHibernate;
import ru.otus.domain.dao.ClientDao;
import ru.otus.domain.dao.ClientDaoException;
import ru.otus.domain.model.Client;
import ru.otus.domain.sessionmanager.SessionManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ClientDaoHibernate implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public ClientDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Client> findByCredentials(String login, String password) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return currentSession.getHibernateSession()
                    .createQuery("select c from Client c where c.login = :login and c.password = :password", Client.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Client> selectAll() {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            return currentSession.getHibernateSession().createQuery("select с from Client с", Client.class).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public long insert(Client client) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(client);
            hibernateSession.flush();
            return client.getId();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
