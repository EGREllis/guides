package net.guides.view.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.PaymentType;
import net.guides.view.Command;

import java.util.Properties;

public class PaymentTypeDeleteCommand extends FacadeCommandTemplate<PaymentType> implements Command<PaymentType> {
    public PaymentTypeDeleteCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "payment.type.delete");
    }

    @Override
    protected boolean doTheJob(PaymentType paymentType) {
        return facade.removePaymentType(paymentType.getId());
    }
}
