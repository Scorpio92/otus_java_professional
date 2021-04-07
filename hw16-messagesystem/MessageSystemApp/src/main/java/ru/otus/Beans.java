package ru.otus;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.otus.data.HibernateUtils;
import ru.otus.domain.handlers.ClientsResponseHandler;
import ru.otus.domain.handlers.GetClientsHandler;
import ru.otus.domain.handlers.SaveClientHandler;
import ru.otus.domain.model.Client;
import ru.otus.domain.model.ClientData;
import ru.otus.domain.service.ClientService;
import ru.otus.messagesystem.*;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

import javax.annotation.PostConstruct;

@Configuration
public class Beans {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String DB_MIGRATION_SCRIPT_DIR = "/db/migration";

    @Autowired
    private Flyway flyway;

    @PostConstruct
    public void dbMigrate() {
        flyway.migrate();
    }

    @Bean
    public org.hibernate.cfg.Configuration hibernateConfig() {
        return new org.hibernate.cfg.Configuration().configure(HIBERNATE_CFG_FILE);
    }

    @Bean
    public SessionFactory hibernateSessionFactory(org.hibernate.cfg.Configuration configuration) {
        return HibernateUtils.buildSessionFactory(configuration, Client.class);
    }

    @Bean
    public Flyway flywayMigrations(org.hibernate.cfg.Configuration configuration) {
        return Flyway.configure()
                .dataSource(
                        configuration.getProperty("hibernate.connection.url"),
                        configuration.getProperty("hibernate.connection.username"),
                        configuration.getProperty("hibernate.connection.password")
                )
                .locations("classpath:".concat(DB_MIGRATION_SCRIPT_DIR))
                .load();
    }

    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean
    @Scope(value = "prototype")
    public HandlersStore requestHandlerDatabaseStore() {
        return new HandlersStoreImpl();
    }

    @Bean("dbClient")
    public MsClient dbMsClient(MessageSystem messageSystem, HandlersStore requestHandlerDatabaseStore,
                               CallbackRegistry callbackRegistry, ClientService clientService) {
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_USERS, new GetClientsHandler(clientService));
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_USER, new SaveClientHandler(clientService));
        MsClient msClient = new MsClientImpl("dbClient", messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(msClient);
        return msClient;
    }

    @Bean("frontClient")
    public MsClient frontMsClient(MessageSystem messageSystem, HandlersStore requestHandlerDatabaseStore,
                                  CallbackRegistry callbackRegistry) {
        RequestHandler<ClientData> requestHandler = new ClientsResponseHandler(callbackRegistry);
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_USERS, requestHandler);
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_USER, requestHandler);
        MsClient msClient = new MsClientImpl("frontClient", messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(msClient);
        return msClient;
    }
}
