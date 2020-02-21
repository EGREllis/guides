package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.controller.Command;

import java.util.Properties;

public class ClientEditCommand extends FacadeCommandTemplate<Client> implements Command<Client> {
    public ClientEditCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "client.edit");
    }

    @Override
    protected boolean doTheJob(Client client) {
        return facade.updateClient(client);
    }
}
