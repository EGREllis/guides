package net.guides.view.web.listener;

import net.guides.data.DataAccessFacade;
import net.guides.data.InMemoryDataAccessFacade;
import net.guides.data.file.FileInMemoryDataAccessFacade;
import net.guides.data.jdbc.*;
import net.guides.derby.Database;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.PaymentType;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GuideStartupListener implements ServletContextListener {
    public static final String DATA_ACCESS_FACADE_KEY = "DataAccessFacade";
    public static final String DATA_ACCESS_MODE_KEY = "DataAccessMode";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String dataMode = sce.getServletContext().getInitParameter(DATA_ACCESS_MODE_KEY).toLowerCase();
        Properties properties = new Properties();
        try (InputStream input = ClassLoader.getSystemResourceAsStream("web.properties")) {
            properties.load(input);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        DataAccessFacade dataAccessFacade;
        if ("file".equals(dataMode)) {
            System.out.println("Data access facade loadin from files");
            dataAccessFacade = new FileInMemoryDataAccessFacade(properties);
        } else if ("db".equals(dataMode)) {
            System.out.println("Data access facade loading from database");
            DateFormat dateFormat = new SimpleDateFormat(properties.getProperty(Database.DATABASE_DATE_FORMAT));
            Database database = new Database();
            try {
                database.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ClientJDBCReader clientJDBCReader = new ClientJDBCReader();
            EventJDBCReader eventJDBCReader = new EventJDBCReader();
            PaymentTypeJDBCReader paymentTypeJDBCReader = new PaymentTypeJDBCReader();

            dataAccessFacade = new JdbcDataAccessFacade(database, dateFormat, clientJDBCReader, eventJDBCReader, paymentTypeJDBCReader, null);

            Map<Integer,Client> clientMap = new HashMap<>();
            for (Client client : dataAccessFacade.getAllClients()) {
                clientMap.put(client.getId(), client);
            }

            Map<Integer,Event> eventMap = new HashMap<>();
            for (Event event : dataAccessFacade.getAllEvents()) {
                eventMap.put(event.getId(), event);
            }

            Map<Integer,PaymentType> paymentTypeMap = new HashMap<>();
            for (PaymentType paymentType : dataAccessFacade.getAllPaymentTypes()) {
                paymentTypeMap.put(paymentType.getId(), paymentType);
            }

            dataAccessFacade = new JdbcDataAccessFacade(database, dateFormat, clientJDBCReader, eventJDBCReader, paymentTypeJDBCReader, new PaymentJDBCReader(clientMap, eventMap, paymentTypeMap));
        } else {
            System.out.println("Data access facade defaulted to in-memory");
            dataAccessFacade = new InMemoryDataAccessFacade();
        }
        sce.getServletContext().setAttribute(DATA_ACCESS_FACADE_KEY, dataAccessFacade);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
