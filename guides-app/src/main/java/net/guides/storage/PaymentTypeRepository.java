package net.guides.storage;

import net.guides.model.PaymentType;

import java.util.List;

public interface PaymentTypeRepository {
    List<PaymentType> getPaymentTypes();
}
