package ru.otus.domain.service;

import ru.otus.domain.model.Client;

import java.util.List;

public interface ClientService {

    long saveClient(Client client);

    List<Client> allClients();
}
