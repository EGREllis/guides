package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.PaymentType;
import net.guides.controller.Command;

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
