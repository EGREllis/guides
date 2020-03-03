package net.guides;

import net.guides.controller.Listener;
import net.guides.data.DataAccessFacade;
import net.guides.data.file.FileInMemoryDataAccessFacade;
import net.guides.view.SwingView;

import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String args[]) {
        Properties composedProperties = new Properties();
        try {
            composedProperties.load(ClassLoader.getSystemResourceAsStream("composed.properties"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        final DataAccessFacade dataAccessFacade = new FileInMemoryDataAccessFacade(composedProperties);
        dataAccessFacade.start();
        System.out.println(String.format("Loaded %1$d clients, %2$d events, %3$d payment types, %4$d payments",
                dataAccessFacade.getAllClients().size(),
                dataAccessFacade.getAllEvents().size(),
                dataAccessFacade.getAllPaymentTypes().size(),
                dataAccessFacade.getAllPayments().size()));

        SwingView view = new SwingView(composedProperties, dataAccessFacade);
        view.addListener(new Listener() {
            @Override
            public void alert() {
                dataAccessFacade.stop();
            }
        });
        view.start();
    }
}
