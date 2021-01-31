package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;

import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientDao clientDao;
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(ClientDao clientDao, HwCache<String, Client> cache) {
        this.clientDao = clientDao;
        this.cache = cache;
    }

    @Override
    public long saveClient(Client client) {
        Client cachedClient = cache.get(String.valueOf(client.getId()));
        if (cachedClient == null) {
            return saveInDB(client);
        } else {
            if (cachedClient.equals(client)) return client.getId();
            else return saveInDB(client);
        }
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(String.valueOf(id));
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        } else {
            try (var sessionManager = clientDao.getSessionManager()) {
                sessionManager.beginSession();
                try {
                    Optional<Client> clientOptional = clientDao.findById(id);
                    logger.info("client: {}", clientOptional.orElse(null));
                    clientOptional.ifPresent(client -> cache.put(String.valueOf(id), client));
                    return clientOptional;
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    sessionManager.rollbackSession();
                }
                return Optional.empty();
            }
        }
    }

    private long saveInDB(Client client) {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var clientId = clientDao.insert(client);
                sessionManager.commitSession();
                logger.info("created client: {}", clientId);
                cache.put(String.valueOf(clientId), client);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
