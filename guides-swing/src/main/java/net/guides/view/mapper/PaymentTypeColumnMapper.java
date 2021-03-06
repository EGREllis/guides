package net.guides.view.mapper;

import net.guides.model.PaymentType;
import net.guides.view.ColumnMapper;

public class PaymentTypeColumnMapper implements ColumnMapper<PaymentType> {
    private static final String[] columnNames = new String[] {"Description"};

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    @Override
    public Object getValueAt(int column, PaymentType data) {
        Object result;
        switch (column) {
            case 0:
                result = data.getDescription();
                break;
            default:
                throw new IllegalStateException("This should never be executed... "+column);
        }
        return result;
    }
}
