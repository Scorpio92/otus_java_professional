package ru.otus.domain.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.domain.service.ClientService;
import ru.otus.domain.service.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USER_LIST = "users";

    private final ClientService clientService;
    private final TemplateProcessor templateProcessor;

    public UsersServlet(ClientService clientService, TemplateProcessor templateProcessor) {
        this.clientService = clientService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_USER_LIST, clientService.allClients());
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }
}
