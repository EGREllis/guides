package net.guides.model;

import java.util.Date;

public class Payment implements Identifiable {
    private Integer paymentId;
    private Client client;
    private Event event;
    private PaymentType paymentType;
    private Date paymentDate;

    public Payment(Integer paymentId, Client client, Event event, PaymentType paymentType, Date paymentDate) {
        this.paymentId = paymentId;
        this.client = client;
        this.event = event;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public Client getClient() {
        return client;
    }

    public Event getEventId() {
        return event;
    }

    public PaymentType getPaymentTypeId() {
        return paymentType;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public Payment replaceId(int paymentId) {
        return new Payment(paymentId, client, event, paymentType, paymentDate);
    }

    @Override
    public String toString() {
        return String.format("Payment:%1$d Client:%2$s Event:%3$s PaymentType:%4$s PaymentDate:%5$s", paymentId, client, event, paymentType, paymentDate);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Payment) {
            Payment other = (Payment)obj;
            result = paymentId == other.paymentId && client.equals(client) && event.equals(other.event) && paymentType.equals(other.paymentType) && paymentDate.equals(other.paymentDate);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return paymentId * 13 + client.hashCode() * 7 + event.hashCode() * 5 + paymentType.hashCode() * 3 + paymentDate.hashCode();
    }

    @Override
    public Integer getId() {
        return paymentId;
    }
}
