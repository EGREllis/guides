package net.guides;

import net.guides.controller.Listener;
import net.guides.data.DataAccessFacade;
import net.guides.data.file.FileInMemoryDataAccessFacade;
import net.guides.data.jdbc.*;
import net.guides.derby.Database;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.view.SwingView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static final String DATA_ACCESS_MESSAGE = "Starting data access using source: %1$s";

    public static void main(String args[]) {
        List<String> arguments = new ArrayList<>(args.length);
        for (String arg : args) {
            arguments.add(arg.toLowerCase());
        }

        Mode mode = Mode.FILE;
        if (arguments.contains("--file")) {
            mode = Mode.FILE;
        } else if (arguments.contains("--db")) {
            mode = Mode.DB;
        }

        Properties composedProperties = new Properties();
        try {
            composedProperties.load(ClassLoader.getSystemResourceAsStream("composed.properties"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        final DataAccessFacade dataAccessFacade;
        if (Mode.FILE.equals(mode)) {
            System.out.println(String.format(DATA_ACCESS_MESSAGE, Mode.FILE));
            dataAccessFacade = new FileInMemoryDataAccessFacade(composedProperties);
        } else {
            try {
                System.out.println(String.format(DATA_ACCESS_MESSAGE, Mode.DB));
                Database database = new Database();
                database.start();
                List<String> tables = database.listAllTables();
                int nGuides = 0;
                for (String table : tables) {
                    String[] split = table.split("\\.");
                    if ("GUIDES".equals(split[0])) {
                        nGuides++;
                    }
                }
                System.out.println("Guide tables: "+nGuides);
                System.out.println(tables);
                if (nGuides == 0) {
                    database.create(true, true, true, false);
                }
                DateFormat dateFormat = new SimpleDateFormat(composedProperties.getProperty("sql.date.format"));
                ClientJDBCReader clientReader = new ClientJDBCReader();
                EventJDBCReader eventReader = new EventJDBCReader();
                PaymentTypeJDBCReader paymentTypeReader = new PaymentTypeJDBCReader();

                DataAccessFacade loader = new JdbcDataAccessFacade(database, dateFormat, clientReader, eventReader, paymentTypeReader, null);
                List<Client> clients = loader.getAllClients();
                List<Event> events = loader.getAllEvents();
                List<PaymentType> paymentTypes = loader.getAllPaymentTypes();

                Map<Integer, Client> clientMap = new HashMap<>();
                Map<Integer, Event> eventMap = new HashMap<>();
                Map<Integer, PaymentType> paymentTypeMap = new HashMap<>();
                for (Client client : clients) {
                    clientMap.put(client.getId(), client);
                }
                for (Event event : events) {
                    eventMap.put(event.getId(), event);
                }
                for (PaymentType paymentType : paymentTypes) {
                    paymentTypeMap.put(paymentType.getId(), paymentType);
                }

                PaymentJDBCReader paymentReader = new PaymentJDBCReader(clientMap, eventMap, paymentTypeMap);
                dataAccessFacade = new JdbcDataAccessFacade(database, dateFormat, clientReader, eventReader, paymentTypeReader, paymentReader);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        dataAccessFacade.start();
        System.out.println(String.format("Loaded %1$d clients, %2$d events, %3$d payment types, %4$d payments",
                dataAccessFacade.getAllClients().size(),
                dataAccessFacade.getAllEvents().size(),
                dataAccessFacade.getAllPaymentTypes().size(),
                dataAccessFacade.getAllPayments().size()));

        SwingView view = SwingView.newSwingView(composedProperties, dataAccessFacade);
        view.addListener(new Listener() {
            @Override
            public void alert() {
                dataAccessFacade.stop();
            }
        });
        view.start();
    }

    private enum Mode {
        FILE,
        DB;
    }
}
