package ru.otus.domain.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.domain.model.Client;
import ru.otus.domain.service.ClientService;

import java.io.IOException;
import java.util.stream.Collectors;


public class UsersApiServlet extends HttpServlet {

    private final ClientService clientService;
    private final Gson gson;

    public UsersApiServlet(ClientService clientService, Gson gson) {
        this.clientService = clientService;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestData = req.getReader().lines().collect(Collectors.joining());
        Client clientForSave = gson.fromJson(requestData, Client.class);
        long id = clientService.saveClient(clientForSave);
        resp.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = resp.getOutputStream();
        out.print(gson.toJson(id));
    }
}
