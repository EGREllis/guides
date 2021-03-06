package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Payment;
import net.guides.controller.Command;

import java.util.Properties;

public class PaymentEditCommand extends FacadeCommandTemplate<Payment> implements Command<Payment> {
    public PaymentEditCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "payment.edit");
    }

    @Override
    protected boolean doTheJob(Payment payment) {
        return facade.updatePayment(payment);
    }
}
