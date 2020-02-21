package net.guides.view.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.view.Command;

import java.util.Properties;

public class ClientAddCommand extends FacadeCommandTemplate<Client> implements Command<Client> {
    public ClientAddCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "client.add");
    }

    @Override
    protected boolean doTheJob(Client client) {
        return facade.addClient(client);
    }
}
