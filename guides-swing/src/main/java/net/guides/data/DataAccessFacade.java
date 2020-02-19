package net.guides.data;

import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.util.List;

public interface DataAccessFacade {
    List<Client> getAllClients();
    Client getClient(int clientId);
    boolean addClient(Client client);
    boolean removeClient(int clientId);
    List<Event> getAllEvents();
    Event getEvent(int eventId);
    boolean addEvent(Event event);
    boolean removeEvent(int eventId);
    List<PaymentType> getAllPaymentTypes();
    PaymentType getPaymentType(int paymentTypeId);
    boolean addPaymentType(PaymentType paymentType);
    boolean removePaymentType(int paymentTypeId);
    List<Payment> getPayments();
    Payment getPayment(int paymentId);
    boolean addPayment(Payment payment);
    boolean removePayment(int paymentId);
}
