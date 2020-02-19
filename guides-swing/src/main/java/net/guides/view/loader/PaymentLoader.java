package net.guides.view.loader;

import net.guides.data.DataAccessFacade;
import net.guides.model.Payment;
import net.guides.view.Loader;

import java.util.List;

public class PaymentLoader implements Loader<Payment> {
    private final DataAccessFacade facade;

    public PaymentLoader(DataAccessFacade facade) {
        this.facade = facade;
    }

    @Override
    public List<Payment> load() {
        return facade.getAllPayments();
    }
}
