package net.guides.view.format;

import net.guides.model.PaymentType;
import net.guides.view.Formatter;

import java.util.Properties;

public class PaymentTypeFormatter implements Formatter<PaymentType> {
    private static final String PAYMENT_TYPE_FORMATTER_KEY = "payment.type.combo.box.format";
    private final String format;

    public PaymentTypeFormatter(Properties properties) {
        this.format = properties.getProperty(PAYMENT_TYPE_FORMATTER_KEY);
    }

    @Override
    public String format(PaymentType paymentType) {
        return String.format(format, paymentType.getDescription(), paymentType.getId());
    }
}
