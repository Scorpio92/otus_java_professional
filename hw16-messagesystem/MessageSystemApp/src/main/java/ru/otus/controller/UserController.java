package ru.otus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.domain.model.Client;
import ru.otus.domain.model.ClientData;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final MsClient frontMsClient;
    private final MsClient dbMsClient;
    private final SimpMessagingTemplate template;

    @Autowired
    public UserController(@Qualifier("frontClient") MsClient frontMsClient, @Qualifier("dbClient") MsClient dbMsClient,
                          SimpMessagingTemplate template) {
        this.frontMsClient = frontMsClient;
        this.dbMsClient = dbMsClient;
        this.template = template;
    }

    @MessageMapping("/users")
    public void users() {
        logger.info("get user list");
        Message outMsg = frontMsClient.produceMessage(
                dbMsClient.getName(),
                ClientData.empty(),
                MessageType.ALL_USERS,
                resultDataType -> template.convertAndSend("/topic/response", resultDataType.clients())
        );
        frontMsClient.sendMessage(outMsg);
    }

    @MessageMapping("/createUser")
    public void createUser(Client client) {
        logger.info("create user {}", client);
        Message outMsg = frontMsClient.produceMessage(
                dbMsClient.getName(),
                ClientData.fromOne(client),
                MessageType.SAVE_USER,
                resultDataType -> users()
        );
        frontMsClient.sendMessage(outMsg);
    }
}
