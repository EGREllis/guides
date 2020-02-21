package net.guides.view.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;
import net.guides.model.PaymentType;
import net.guides.view.Command;

import java.util.Properties;

public class PaymentTypeAddCommand extends FacadeCommandTemplate<PaymentType> implements Command<PaymentType> {
    public PaymentTypeAddCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "payment.type.add");
    }

    @Override
    protected boolean doTheJob(PaymentType paymentType) {
        return facade.addPaymentType(paymentType);
    }
}
