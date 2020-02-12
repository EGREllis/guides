package net.guides.storage;

import net.guides.model.Payment;

import java.util.List;

public interface PaymentRepository {
    List<Payment> getPayments();
    boolean addPayment(Payment payment);
}
