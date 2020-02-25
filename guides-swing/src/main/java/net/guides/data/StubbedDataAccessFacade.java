package net.guides.data;

import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class StubbedDataAccessFacade implements DataAccessFacade {
    private static final List<Client> clients = Arrays.asList(
            new Client(1, "Gary", "Blower", "07853000000", "a@b"),
            new Client(2, "Bob","Coppins", "07853200900", "bob@evil")
    );
    private static final List<Event> events = Arrays.asList(new Event(1, "Demo event", new Date()));
    private static final List<PaymentType> paymentTypes = Arrays.asList(new PaymentType(1, "Card"));
    private static final List<Payment> payments = Arrays.asList(new Payment(1, clients.get(0), events.get(0), paymentTypes.get(0), new Date()));

    @Override
    public List<Client> getAllClients() {
        return clients;
    }

    @Override
    public Client getClient(int clientId) {
        return clients.get(clientId);
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
        return events;
    }

    @Override
    public Event getEvent(int eventId) {
        return events.get(eventId);
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
        return paymentTypes;
    }

    @Override
    public PaymentType getPaymentType(int paymentTypeId) {
        return paymentTypes.get(paymentTypeId);
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
        return payments;
    }

    @Override
    public Payment getPayment(int paymentId) {
        return payments.get(paymentId);
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
