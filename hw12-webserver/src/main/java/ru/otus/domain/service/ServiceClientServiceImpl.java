package ru.otus.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.domain.dao.ClientDao;
import ru.otus.domain.model.Client;

import java.util.ArrayList;

public class ServiceClientServiceImpl implements UserAuthService, ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceClientServiceImpl.class);

    private final ClientDao clientDao;

    public ServiceClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        boolean result = false;
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                result = clientDao.findByCredentials(login, password).isPresent();
                logger.info("client with login {} check result {}", login, result);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
        } catch (Exception e) {
            logger.error("Failed authenticate", e);
        }
        return result;
    }

    @Override
    public long saveClient(Client client) {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var clientId = clientDao.insert(client);
                sessionManager.commitSession();
                logger.info("created client: {}", clientId);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Iterable<Client> allClients() {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                return new ArrayList<>(clientDao.selectAll());
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
