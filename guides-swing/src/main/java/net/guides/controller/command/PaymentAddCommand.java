package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Payment;
import net.guides.controller.Command;

import java.util.Properties;

public class PaymentAddCommand extends FacadeCommandTemplate<Payment> implements Command<Payment> {
    public PaymentAddCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "payment.add");
    }

    @Override
    protected boolean doTheJob(Payment payment) {
        return facade.addPayment(payment);
    }
}
