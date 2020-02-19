package net.guides.view.loader;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.view.Loader;

import java.util.List;

public class ClientLoader implements Loader<Client> {
    private final DataAccessFacade facade;

    public ClientLoader(DataAccessFacade facade) {
        this.facade = facade;
    }

    @Override
    public List<Client> load() {
        return facade.getAllClients();
    }
}
