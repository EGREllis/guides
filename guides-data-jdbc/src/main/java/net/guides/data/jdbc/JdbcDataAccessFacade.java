package net.guides.data.jdbc;

import net.guides.data.DataAccessFacade;
import net.guides.derby.Database;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcDataAccessFacade implements DataAccessFacade {
    private static final String SELECT_ALL_CLIENT_QUERY = "SELECT client_id, first_name, last_name, sms, email FROM guides.client";
    private static final String SELECT_ALL_EVENT_QUERY = "SELECT event_id, title, start_date FROM guides.event";
    private static final String SELECT_ALL_PAYMENT_TYPE_QUERY = "SELECT payment_type_id, description FROM guides.payment_type";
    private static final String SELECT_ALL_PAYMENT = "SELECT payment_id, client_id, event_id, payment_type_id, payment_date FROM guides.payment";
    private static final String INSERT_SINGLE_CLIENT_STATEMENT = "INSERT INTO guides.client (first_name, last_name, sms, email) VALUES ('%1$s', '%2$s', '%3$s', '%4$s')";
    private static final String INSERT_SINGLE_EVENT_STATEMENT = "INSERT INTO guides.event (title, start_date) VALUES ('%1$s', '%2$s')";
    private static final String INSERT_PAYMENT_TYPE_STATEMENT = "INSERT INTO guides.payment_type (description) VALUES ('%1$s')";
    private static final String INSERT_PAYMENT_STATEMENT = "INSERT INTO guides.payment (client_id, event_id, payment_type_id, payment_date) VALUES (%1$d, %2$d, %3$d, '%4$s')";
    private static final String SELECT_SINGLE_CLIENT_QUERY = "SELECT client_id, first_name, last_name, sms, email FROM guides.client WHERE client_id = %1$d";
    private static final String SELECT_SINGLE_EVENT_QUERY = "SELECT event_id, title, start_date FROM guides.event WHERE event_id = %1$d";
    private static final String SELECT_SINGLE_PAYMENT_TYPE_QUERY = "SELECT payment_type_id, description FROM guides.payment_type WHERE payment_type_id = %1$d";
    private static final String SELECT_SINGLE_PAYMENT_QUERY = "SELECT payment_id, client_id, event_id, payment_type_id, payment_date FROM guides.payment WHERE payment_id = %1$d";
    private static final String UPDATE_SINGLE_CLIENT_QUERY = "UPDATE guides.client SET first_name = '%1$s', last_name = '%2$s', sms = '%3$s', email = '%4$s' WHERE client_id = %5$d";
    private static final String UPDATE_SINGLE_EVENT_QUERY = "UPDATE guides.event SET title = '%1$s', start_date = '%2$s' WHERE event_id = %3$d";
    private static final String UPDATE_SINGLE_PAYMENT_TYPE_QUERY = "UPDATE guides.payment_type SET description = '%1$s' WHERE payment_type_id = %2$d";
    private static final String UPDATE_SINGLE_PAYMENT_QUERY = "UPDATE guides.payment SET client_id = %1$d, event_id = %2$d, payment_type_id = %3$d, payment_date = '%4$s' WHERE payment_id = %5$d";
    private static final String DELETE_SINGLE_CLIENT_QUERY = "DELETE FROM guides.client WHERE client_id = %1$d";
    private static final String DELETE_SINGLE_EVENT_QUERY = "DELETE FROM guides.event WHERE event_id = %1$d";
    private static final String DELETE_SINGLE_PAYMENT_TYPE_QUERY = "DELETE FROM guides.payment_type WHERE payment_type_id = %1$d";
    private static final String DELETE_SINGLE_PAYMENT_QUERY = "DELETE FROM guides.payment WHERE payment_id = %1$d";

    private final Database database;
    private final ClientJDBCReader clientReader;
    private final EventJDBCReader eventReader;
    private final PaymentTypeJDBCReader paymentTypeReader;
    private final PaymentJDBCReader paymentReader;
    private final DateFormat dateFormat;

    public JdbcDataAccessFacade(Database database, DateFormat dateFormat, ClientJDBCReader clientReader, EventJDBCReader eventReader, PaymentTypeJDBCReader paymentTypeReader, PaymentJDBCReader paymentReader) {
        this.database = database;
        this.clientReader = clientReader;
        this.eventReader = eventReader;
        this.paymentTypeReader = paymentTypeReader;
        this.paymentReader = paymentReader;
        this.dateFormat = dateFormat;
    }

    @Override
    public void start() {
        // Create/connect to database
        // Check if schema is present
        // Create schema if not
    }

    @Override
    public void stop() {
    }

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = SELECT_ALL_CLIENT_QUERY;
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            Map<Integer, Client> clientsMap = clientReader.getResultsMap(resultSet);
            clients.addAll(clientsMap.values());
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return clients;
    }

    @Override
    public Client getClient(int clientId) {
        Client client;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(SELECT_SINGLE_CLIENT_QUERY, clientId);
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            client = clientReader.readResultSet(resultSet);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return client;
    }

    @Override
    public boolean addClient(Client client) {
        boolean added;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(INSERT_SINGLE_CLIENT_STATEMENT, client.getFirstName(), client.getLastName(), client.getSms(), client.getEmail());
            System.out.println(sql);
            statement.execute(sql);
            added = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return added;
    }

    @Override
    public boolean updateClient(Client client) {
        boolean updated;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(UPDATE_SINGLE_CLIENT_QUERY, client.getFirstName(), client.getLastName(), client.getSms(), client.getEmail(), client.getId());
            System.out.println(sql);
            statement.execute(sql);
            updated = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return updated;
    }

    @Override
    public boolean removeClient(int clientId) {
        boolean deleted;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(DELETE_SINGLE_CLIENT_QUERY, clientId);
            System.out.println(sql);
            deleted = statement.execute(sql);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return deleted;
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = SELECT_ALL_EVENT_QUERY;
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            Map<Integer, Event> results = eventReader.getResultsMap(resultSet);
            events.addAll(results.values());
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return events;
    }

    @Override
    public Event getEvent(int eventId) {
        Event event;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(SELECT_SINGLE_EVENT_QUERY, eventId);
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            event = eventReader.readResultSet(resultSet);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return event;
    }

    @Override
    public boolean addEvent(Event event) {
        boolean added;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(INSERT_SINGLE_EVENT_STATEMENT, event.getTitle(), dateFormat.format(event.getStartDate()));
            System.out.println(sql);
            statement.execute(sql);
            added = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return added;
    }

    @Override
    public boolean updateEvent(Event event) {
        boolean updated;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(UPDATE_SINGLE_EVENT_QUERY, event.getTitle(), dateFormat.format(event.getStartDate()), event.getEventId());
            System.out.println(sql);
            statement.execute(sql);
            updated = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return updated;
    }

    @Override
    public boolean removeEvent(int eventId) {
        boolean deleted;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(String.format(DELETE_SINGLE_EVENT_QUERY, eventId));
            System.out.println(sql);
            statement.execute(sql);
            deleted = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return deleted;
    }

    @Override
    public List<PaymentType> getAllPaymentTypes() {
        List<PaymentType> paymentTypes = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = SELECT_ALL_PAYMENT_TYPE_QUERY;
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            Map<Integer,PaymentType> records = paymentTypeReader.getResultsMap(resultSet);
            paymentTypes.addAll(records.values());
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return paymentTypes;
    }

    @Override
    public PaymentType getPaymentType(int paymentTypeId) {
        PaymentType paymentType;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(SELECT_SINGLE_PAYMENT_TYPE_QUERY, paymentTypeId);
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            paymentType = paymentTypeReader.readResultSet(resultSet);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return paymentType;
    }

    @Override
    public boolean addPaymentType(PaymentType paymentType) {
        boolean added;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(INSERT_PAYMENT_TYPE_STATEMENT, paymentType.getDescription(), paymentType.getId());
            System.out.println(sql);
            statement.execute(sql);
            added = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return added;
    }

    @Override
    public boolean updatePaymentType(PaymentType paymentType) {
        boolean updateCount;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(UPDATE_SINGLE_PAYMENT_TYPE_QUERY, paymentType.getDescription(), paymentType.getId());
            System.out.println(sql);
            statement.execute(sql);
            updateCount = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return updateCount;
    }

    @Override
    public boolean removePaymentType(int paymentTypeId) {
        boolean paymentType;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(DELETE_SINGLE_PAYMENT_TYPE_QUERY, paymentTypeId);
            System.out.println(sql);
            statement.execute(sql);
            paymentType = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return paymentType;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = SELECT_ALL_PAYMENT;
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            Map<Integer,Payment> paymentMap = paymentReader.getResultsMap(resultSet);
            payments.addAll(paymentMap.values());
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return payments;
    }

    @Override
    public Payment getPayment(int paymentId) {
        Payment payment;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(SELECT_SINGLE_PAYMENT_QUERY, paymentId);
            System.out.println(sql);
            statement.execute(sql);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            payment = paymentReader.readResultSet(resultSet);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return payment;
    }

    @Override
    public boolean addPayment(Payment payment) {
        boolean added;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(INSERT_PAYMENT_STATEMENT, payment.getClient().getId(), payment.getEventId().getEventId(), payment.getPaymentTypeId().getId(), dateFormat.format(payment.getPaymentDate()));
            System.out.println(sql);
            statement.execute(sql);
            added = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return added;
    }

    @Override
    public boolean updatePayment(Payment payment) {
        boolean updated;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(UPDATE_SINGLE_PAYMENT_QUERY, payment.getClient().getId(), payment.getEventId().getId(), payment.getPaymentTypeId().getId(), dateFormat.format(payment.getPaymentDate()), payment.getId());
            System.out.println(sql);
            statement.execute(sql);
            updated = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return updated;
    }

    @Override
    public boolean removePayment(int paymentId) {
        boolean deleted;
        try {
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String sql = String.format(DELETE_SINGLE_PAYMENT_QUERY, paymentId);
            System.out.println(sql);
            statement.execute(sql);
            deleted = statement.getUpdateCount() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return deleted;
    }
}
