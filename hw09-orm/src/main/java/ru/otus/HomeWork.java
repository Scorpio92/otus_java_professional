package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.Client;
import ru.otus.core.service.DBServiceAccountImpl;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.AccountDaoJdbc;
import ru.otus.jdbc.dao.ClientDaoJdbc;
import ru.otus.jdbc.mapper.JdbcMapper;
import ru.otus.jdbc.mapper.impl.DefaultJdbcMapper;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

// Общая часть
        var dataSource = new DataSourceDemo();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Client> jdbcMapperClient = new DefaultJdbcMapper<>(sessionManager, dbExecutor); //
        ClientDao clientDao = new ClientDaoJdbc(sessionManager, jdbcMapperClient);

// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var clientRowNum = dbServiceClient.saveClient(new Client(5, "vasya", 30));
        logger.info("client row num:{}", clientRowNum);
        Optional<Client> clientOptional = dbServiceClient.getClient(5);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name:{}", client.getName()),
                () -> logger.info("client was not created")
        );

// Работа со счетом
        DbExecutorImpl<Account> dbAccountExecutor = new DbExecutorImpl<>();
        JdbcMapper<Account> jdbcMapperAccount = new DefaultJdbcMapper<>(sessionManager, dbAccountExecutor); //
        AccountDao accountDao = new AccountDaoJdbc(sessionManager, jdbcMapperAccount);

        var dbServiceAccount = new DBServiceAccountImpl(accountDao);
        var accountRowNum = dbServiceAccount.saveAccount(new Account("abc123", "xxx", 123.456));
        logger.info("account row num:{}", accountRowNum);
        Optional<Account> accountOptional = dbServiceAccount.getAccount("abc123");

        accountOptional.ifPresentOrElse(
                account -> logger.info("created account, number:{}", account.getNo()),
                () -> logger.info("account was not created")
        );
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
