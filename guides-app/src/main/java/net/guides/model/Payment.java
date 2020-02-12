package net.guides.model;

import java.util.Date;

public class Payment {
    private int paymentId;
    private int clientId;
    private int eventId;
    private PaymentType paymentType;
    private Date paymentDate;

    public Payment(int paymentId, int clientId, int eventId, PaymentType paymentType, Date paymentDate) {
        this.paymentId = paymentId;
        this.clientId = clientId;
        this.eventId = eventId;
        this.paymentType = paymentType;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getEventId() {
        return eventId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    @Override
    public String toString() {
        return String.format("PaymentType:%1$d ClientId:%2$d EventId:%3$d PaymentType:%4$s PaymentDate:%5$s", paymentId, clientId, eventId, paymentType, paymentDate);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Payment) {
            Payment other = (Payment)obj;
            result = paymentId == other.paymentId && clientId == other.clientId && eventId == other.eventId && paymentType == other.paymentType && paymentDate.equals(other.paymentDate);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return paymentId * 13 + clientId * 7 + eventId * 5 + paymentType.hashCode() * 3 + paymentDate.hashCode();
    }
}
