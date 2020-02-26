package net.guides.data.jdbc;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.util.List;

public class JdbcDataAccessFacade implements DataAccessFacade {


    @Override
    public List<Client> getAllClients() {
        return null;
    }

    @Override
    public Client getClient(int clientId) {
        return null;
    }

    @Override
    public boolean addClient(Client client) {
        return false;
    }

    @Override
    public boolean updateClient(Client client) {
        return false;
    }

    @Override
    public boolean removeClient(int clientId) {
        return false;
    }

    @Override
    public List<Event> getAllEvents() {
        return null;
    }

    @Override
    public Event getEvent(int eventId) {
        return null;
    }

    @Override
    public boolean addEvent(Event event) {
        return false;
    }

    @Override
    public boolean updateEvent(Event event) {
        return false;
    }

    @Override
    public boolean removeEvent(int eventId) {
        return false;
    }

    @Override
    public List<PaymentType> getAllPaymentTypes() {
        return null;
    }

    @Override
    public PaymentType getPaymentType(int paymentTypeId) {
        return null;
    }

    @Override
    public boolean addPaymentType(PaymentType paymentType) {
        return false;
    }

    @Override
    public boolean updatePaymentType(PaymentType paymentType) {
        return false;
    }

    @Override
    public boolean removePaymentType(int paymentTypeId) {
        return false;
    }

    @Override
    public List<Payment> getAllPayments() {
        return null;
    }

    @Override
    public Payment getPayment(int paymentId) {
        return null;
    }

    @Override
    public boolean addPayment(Payment payment) {
        return false;
    }

    @Override
    public boolean updatePayment(Payment payment) {
        return false;
    }

    @Override
    public boolean removePayment(int paymentId) {
        return false;
    }
}
