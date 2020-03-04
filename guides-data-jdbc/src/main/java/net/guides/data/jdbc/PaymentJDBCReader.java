package net.guides.data.jdbc;

import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class PaymentJDBCReader extends JDBCReaderTemplate<Payment> implements JDBCReader<Payment> {
    private Map<Integer,Client> clients;
    private Map<Integer,Event> events;
    private Map<Integer,PaymentType> paymentTypes;

    public PaymentJDBCReader(Map<Integer,Client> clients,
                             Map<Integer,Event> events,
                             Map<Integer,PaymentType> paymentTypes) {
        this.clients = clients;
        this.events = events;
        this.paymentTypes = paymentTypes;
    }

    @Override
    protected Payment readResultSet(ResultSet resultSet) {
        try {
            Integer paymentId = resultSet.getInt("payment_id");
            Integer clientId = resultSet.getInt("client_id");
            Integer eventId = resultSet.getInt("event_id");
            Integer paymentTypeId = resultSet.getInt("payment_type_id");
            Date paymentDate = resultSet.getDate("payment_date");
            Payment payment = new Payment(paymentId,
                    clients.get(clientId),
                    events.get(eventId),
                    paymentTypes.get(paymentTypeId),
                    paymentDate);
            return payment;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }
}
