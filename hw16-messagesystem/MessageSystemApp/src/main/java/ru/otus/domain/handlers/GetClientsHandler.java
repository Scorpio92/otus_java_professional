package ru.otus.domain.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.domain.model.ClientData;
import ru.otus.domain.service.ClientService;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;

import java.util.Optional;

@Component
public class GetClientsHandler implements RequestHandler<ClientData> {

    private static final Logger logger = LoggerFactory.getLogger(GetClientsHandler.class);

    private final ClientService clientService;

    @Autowired
    public GetClientsHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        try {
            ClientData clientData = new ClientData(clientService.allClients());
            return Optional.of(MessageBuilder.buildReplyMessage(MessageType.ALL_USERS, msg, clientData));
        } catch (Exception e) {
            logger.error("failed to get clients list", e);
        }
        return Optional.empty();
    }
}
