package net.guides.view.format;

import net.guides.model.Client;
import net.guides.view.Formatter;

import java.util.Properties;

public class ClientFormatter implements Formatter<Client> {
    private static final String CLIENT_FORMATTER_KEY = "client.combo.box.format";
    private final String format;

    public ClientFormatter(Properties properties) {
        this.format = properties.getProperty(CLIENT_FORMATTER_KEY);
    }

    @Override
    public String format(Client client) {
        return String.format(format, client.getFirstName(), client.getLastName(), client.getSms(), client.getEmail());
    }
}
