package ru.otus.domain.service;

import ru.otus.domain.model.Client;

public interface ClientService {

    long saveClient(Client client);

    Iterable<Client> allClients();
}
