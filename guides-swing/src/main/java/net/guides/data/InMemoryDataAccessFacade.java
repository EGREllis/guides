package net.guides.data;

import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.util.*;

public class InMemoryDataAccessFacade implements DataAccessFacade {
    private Map<Integer,Client> clients = new TreeMap<>();
    private Map<Integer,Event> events = new TreeMap<>();
    private Map<Integer,PaymentType> paymentTypes = new TreeMap<>();
    private Map<Integer,Payment> payments = new TreeMap<>();

    @Override
    public List<Client> getAllClients() {
        List clientCopy = new ArrayList<>(clients.size());
        clientCopy.addAll(clients.values());
        return clientCopy;
    }

    @Override
    public Client getClient(int clientId) {
        return clients.get(clientId);
    }

    @Override
    public boolean addClient(Client client) {
        int newId = getNextId(clients.keySet());
        clients.put(newId, client.replaceId(newId));
        return true;
    }

    @Override
    public boolean updateClient(Client client) {
        clients.put(client.getClientId(), client);
        return true;
    }

    @Override
    public boolean removeClient(int clientId) {
        clients.remove(clientId);
        return true;
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> eventCopy = new ArrayList<>(events.size());
        eventCopy.addAll(events.values());
        return Collections.unmodifiableList(eventCopy);
    }

    @Override
    public Event getEvent(int eventId) {
        return events.get(eventId);
    }

    @Override
    public boolean addEvent(Event event) {
        int newId = getNextId(events.keySet());
        events.put(newId, event.replaceId(newId));
        return true;
    }

    @Override
    public boolean updateEvent(Event event) {
        events.put(event.getEventId(), event);
        return true;
    }

    @Override
    public boolean removeEvent(int eventId) {
        events.remove(eventId);
        return true;
    }

    @Override
    public List<PaymentType> getAllPaymentTypes() {
        List<PaymentType> paymentTypeCopy = new ArrayList<>(paymentTypes.size());
        paymentTypeCopy.addAll(paymentTypes.values());
        return paymentTypeCopy;
    }

    @Override
    public PaymentType getPaymentType(int paymentTypeId) {
        return paymentTypes.get(paymentTypeId);
    }

    @Override
    public boolean addPaymentType(PaymentType paymentType) {
        int newId = getNextId(paymentTypes.keySet());
        paymentTypes.put(newId, paymentType.replaceId(newId));
        return true;
    }

    @Override
    public boolean updatePaymentType(PaymentType paymentType) {
        paymentTypes.put(paymentType.getId(), paymentType);
        return true;
    }

    @Override
    public boolean removePaymentType(int paymentTypeId) {
        paymentTypes.remove(paymentTypeId);
        return true;
    }

    @Override
    public List<Payment> getAllPayments() {
        List<Payment> copy = new ArrayList<>(payments.size());
        copy.addAll(payments.values());
        return copy;
    }

    @Override
    public Payment getPayment(int paymentId) {
        return payments.get(paymentId);
    }

    @Override
    public boolean addPayment(Payment payment) {
        int newId = getNextId(payments.keySet());
        payments.put(newId, payment.replaceId(newId));
        return true;
    }

    @Override
    public boolean updatePayment(Payment payment) {
        payments.put(payment.getPaymentId(), payment);
        return true;
    }

    @Override
    public boolean removePayment(int paymentId) {
        payments.remove(paymentId);
        return true;
    }

    private int getNextId(Set<Integer> ids) {
        Integer maxId = 0;
        for (Integer id : ids) {
            if (maxId < id) {
                maxId = id;
            }
        }
        int newId = maxId+1;
        return newId;
    }

    public static DataAccessFacade stockedWithDummyData() {
        DataAccessFacade facade = new InMemoryDataAccessFacade();
        Client client1 = new Client(1, "Gary", "Blower", "07853000000", "a@b");
        Event event1 = new Event(1, "Example event", new Date());
        PaymentType paymentType1 = new PaymentType(1, "Cash");
        facade.addClient(client1);
        facade.addClient(new Client(2, "Bob","Coppins", "07853200900", "bob@evil"));
        facade.addEvent(event1);
        facade.addPaymentType(paymentType1);
        facade.addPaymentType(new PaymentType(2, "Card"));
        facade.addPayment(new Payment(1, client1, event1, paymentType1, new Date()));
        return facade;
    }
}
