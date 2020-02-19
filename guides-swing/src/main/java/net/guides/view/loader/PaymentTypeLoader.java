package net.guides.view.loader;

import net.guides.data.DataAccessFacade;
import net.guides.model.PaymentType;
import net.guides.view.Loader;

import java.util.List;

public class PaymentTypeLoader implements Loader<PaymentType> {
    private DataAccessFacade facade;

    public PaymentTypeLoader(DataAccessFacade facade) {
        this.facade = facade;
    }

    @Override
    public List<PaymentType> load() {
        return facade.getAllPaymentTypes();
    }
}
