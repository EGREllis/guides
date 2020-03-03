package net.guides.data;

import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.util.List;

public class LoggingDataAccessFacadeDecorator implements DataAccessFacade {
    private final DataAccessFacade decorated;

    public LoggingDataAccessFacadeDecorator(DataAccessFacade dataAccessFacade) {
        this.decorated = dataAccessFacade;
    }

    @Override
    public void start() {
        log("Starting "+decorated);
        decorated.start();
    }

    @Override
    public void stop() {
        log("Stopping "+decorated);
        decorated.stop();
    }

    @Override
    public List<Client> getAllClients() {
        log("Getting all clients...");
        return decorated.getAllClients();
    }

    @Override
    public Client getClient(int clientId) {
        log(String.format("Getting client: %1$d", clientId));
        return decorated.getClient(clientId);
    }

    @Override
    public boolean addClient(Client client) {
        log(String.format("Adding client: %1$s", client));
        return decorated.addClient(client);
    }

    @Override
    public boolean updateClient(Client client) {
        log(String.format("Updating client: %1$s", client));
        return decorated.updateClient(client);
    }

    @Override
    public boolean removeClient(int clientId) {
        log(String.format("Removing client %1$d", clientId));
        return decorated.removeClient(clientId);
    }

    @Override
    public List<Event> getAllEvents() {
        log("Getting all events...");
        return decorated.getAllEvents();
    }

    @Override
    public Event getEvent(int eventId) {
        log(String.format("Gettting event: %1$d", eventId));
        return decorated.getEvent(eventId);
    }

    @Override
    public boolean addEvent(Event event) {
        log(String.format("Adding event: %1$s", event));
        return decorated.addEvent(event);
    }

    @Override
    public boolean updateEvent(Event event) {
        log(String.format("Updating event: %1$s", event));
        return decorated.updateEvent(event);
    }

    @Override
    public boolean removeEvent(int eventId) {
        log(String.format("Remove event: %1$d", eventId));
        return decorated.removeEvent(eventId);
    }

    @Override
    public List<PaymentType> getAllPaymentTypes() {
        log("Getting all payment types");
        return decorated.getAllPaymentTypes();
    }

    @Override
    public PaymentType getPaymentType(int paymentTypeId) {
        log(String.format("Getting payment type: %1$d", paymentTypeId));
        return decorated.getPaymentType(paymentTypeId);
    }

    @Override
    public boolean addPaymentType(PaymentType paymentType) {
        log(String.format("Adding payment type: %1$s", paymentType));
        return decorated.addPaymentType(paymentType);
    }

    @Override
    public boolean updatePaymentType(PaymentType paymentType) {
        log(String.format("Update payment type: %1$s", paymentType));
        return decorated.updatePaymentType(paymentType);
    }

    @Override
    public boolean removePaymentType(int paymentTypeId) {
        log(String.format("Remove payment type: %1$d", paymentTypeId));
        return decorated.removePaymentType(paymentTypeId);
    }

    @Override
    public List<Payment> getAllPayments() {
        log("Getting all payments");
        return decorated.getAllPayments();
    }

    @Override
    public Payment getPayment(int paymentId) {
        log(String.format("Get payment: %1$d", paymentId));
        return decorated.getPayment(paymentId);
    }

    @Override
    public boolean addPayment(Payment payment) {
        log(String.format("Add payment: %1$s", payment));
        return decorated.addPayment(payment);
    }

    @Override
    public boolean updatePayment(Payment payment) {
        log(String.format("Update payment: %1$s", payment));
        return decorated.updatePayment(payment);
    }

    @Override
    public boolean removePayment(int paymentId) {
        log(String.format("Removing payment: %1$d", paymentId));
        return decorated.removePayment(paymentId);
    }

    private void log(String message) {
        System.out.println(message);
        System.out.flush();
    }
}
