package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.Address;
import ru.otus.core.model.Client;
import ru.otus.core.model.Phone;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Arrays;
import java.util.Optional;


public class HomeWork {

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private final HwListener<String, Client> hwListener = HomeWork.this::log;

    public static void main(String[] args) {
        new HomeWork().demo();
    }

    private void demo() {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Account.class, Address.class, Phone.class, Client.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao userDao = new ClientDaoHibernate(sessionManager);
        HwCache<String, Client> cache = new MyCache<>();
        cache.addListener(hwListener);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(userDao, cache);

        Client clientForSave = new Client("Вася", 30, new Account("123", "xxx", 1d), new Address("Java st."));
        clientForSave.setPhones(Arrays.asList(new Phone("123-123"), new Phone("456-456")));
        long id = dbServiceClient.saveClient(clientForSave);

        Optional<Client> mayBeCreatedClient = dbServiceClient.getClient(id);
        mayBeCreatedClient.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));

        Optional<Client> mayBeCreatedClient2 = dbServiceClient.getClient(id);
        mayBeCreatedClient2.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));

        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        anotherGet(dbServiceClient);

        cache.removeListener(hwListener);
    }

    private void anotherGet(DBServiceClient dbServiceClient) {
        Optional<Client> mayBeCreatedClient3 = dbServiceClient.getClient(1);
        mayBeCreatedClient3.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));
    }

    private void outputClient(String header, Client client) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("client:{}", client);
    }

    private void log(String key, Client value, String action) {
        logger.info("Cache. key:{}, value:{}, action: {}", key, value, action);
    }
}
