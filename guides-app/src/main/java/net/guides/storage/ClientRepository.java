package net.guides.storage;

import net.guides.model.Client;

import java.util.List;

public interface ClientRepository {
    List<Client> getClients();
    boolean addClient(Client client);
}
