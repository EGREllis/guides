package net.guides.data.file.writer;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ClientWriter implements Runnable {
    private static final String CLIENT_LINE_FORMAT = "%1$d~%2$s~%3$s~%4$s~%5$s";
    private final DataAccessFacade dataAccessFacade;
    private final String filePath;

    public ClientWriter(DataAccessFacade dataAccessFacade, String filePath) {
        this.dataAccessFacade = dataAccessFacade;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (Writer writer = getWriter(filePath)) {
            List<Client> clients = dataAccessFacade.getAllClients();
            for (Client client : clients) {
                writer.write(String.format(CLIENT_LINE_FORMAT, client.getClientId(), client.getFirstName(), client.getLastName(), client.getSms(), client.getEmail()));
            }
            System.out.println(String.format("Saved %1$d records to client file %2$s", clients.size(), new File(filePath).getAbsolutePath()));
            System.out.flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    Writer getWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }
}
