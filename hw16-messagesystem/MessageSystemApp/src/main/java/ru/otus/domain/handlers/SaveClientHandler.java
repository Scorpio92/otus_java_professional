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
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;

import java.util.Optional;

@Component
public class SaveClientHandler implements RequestHandler<ClientData> {

    private static final Logger logger = LoggerFactory.getLogger(SaveClientHandler.class);

    private final ClientService clientService;

    @Autowired
    public SaveClientHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        try {
            ClientData clientData = MessageHelper.getPayload(msg);
            if (clientData.clients().isEmpty()) throw new IllegalArgumentException("no client for save");
            clientService.saveClient(clientData.clients().get(0));
            return Optional.of(MessageBuilder.buildReplyMessage(MessageType.SAVE_USER, msg, clientData));
        } catch (Exception e) {
            logger.error("failed to save client", e);
        }
        return Optional.empty();
    }
}
