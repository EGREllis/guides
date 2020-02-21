package net.guides.view.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.view.Command;

import java.util.Properties;

public class ClientDeleteCommand extends FacadeCommandTemplate<Client> implements Command<Client> {
    public ClientDeleteCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "client.delete");
    }

    @Override
    protected boolean doTheJob(Client client) {
        return facade.removeClient(client.getClientId());
    }
}
