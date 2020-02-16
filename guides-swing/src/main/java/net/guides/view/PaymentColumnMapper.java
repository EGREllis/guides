package net.guides.view;

import net.guides.model.Payment;

public class PaymentColumnMapper implements ColumnMapper<Payment> {
    private static final String[] columnNames = new String[]{"Client", "Event", "PaymentDate", "PaymentType"};

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    @Override
    public Object getValueAt(int column, Payment data) {
        Object result;
        switch (column) {
            case 0:
                result = data.getClientId();
                break;
            case 1:
                result = data.getEventId();
                break;
            case 2:
                result = data.getPaymentDate();
                break;
            case 3:
                result = data.getPaymentType();
                break;
            default:
                throw new IllegalStateException("This should never be executed... "+column);
        }
        return result;
    }
}
