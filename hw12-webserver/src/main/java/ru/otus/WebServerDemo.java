package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.data.HibernateUtils;
import ru.otus.data.dao.ClientDaoHibernate;
import ru.otus.data.sessionmanager.SessionManagerHibernate;
import ru.otus.domain.model.Client;
import ru.otus.domain.server.UsersWebServer;
import ru.otus.domain.service.ServiceClientServiceImpl;
import ru.otus.domain.service.TemplateProcessor;
import ru.otus.domain.service.TemplateProcessorImpl;

public class WebServerDemo {
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        flywayMigrations(configuration);
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);

        ServiceClientServiceImpl serviceClient = new ServiceClientServiceImpl(new ClientDaoHibernate(new SessionManagerHibernate(sessionFactory)));

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UsersWebServer usersWebServer = new WebServerWithFilterBasedSecurity(serviceClient, serviceClient, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void flywayMigrations(Configuration configuration) {
        var flyway = Flyway.configure()
                .dataSource(
                        configuration.getProperty("hibernate.connection.url"),
                        configuration.getProperty("hibernate.connection.username"),
                        configuration.getProperty("hibernate.connection.password")
                )
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }
}
