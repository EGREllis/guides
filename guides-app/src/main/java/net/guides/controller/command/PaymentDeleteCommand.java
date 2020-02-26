package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Payment;
import net.guides.controller.Command;

import java.util.Properties;

public class PaymentDeleteCommand extends FacadeCommandTemplate<Payment> implements Command<Payment> {
    public PaymentDeleteCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "payment.delete");
    }

    @Override
    protected boolean doTheJob(Payment payment) {
        return facade.removePayment(payment.getPaymentId());
    }
}
