package ru.otus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.domain.model.Client;
import ru.otus.domain.service.ClientService;

@Controller
public class UserController {

    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USER_LIST = "users";
    private static final String TEMPLATE_ATTR_USER = "user";

    private final ClientService clientService;

    @Autowired
    public UserController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/users"})
    public String users(Model model) {
        Iterable<Client> clients = clientService.allClients();
        model.addAttribute(TEMPLATE_ATTR_USER, new Client());
        model.addAttribute(TEMPLATE_ATTR_USER_LIST, clients);
        return USERS_PAGE_TEMPLATE;
    }

    @PostMapping("/user/save")
    public RedirectView saveClient(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return new RedirectView("/", true);
    }
}
