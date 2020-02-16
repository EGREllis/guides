package net.guides.view;

import net.guides.model.Client;

public class ClientColumnMapper implements ColumnMapper<Client> {
    private static final String[] columnNames = new String[] {"First name", "Last name", "SMS", "Email"};

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    @Override
    public Object getValueAt(int column, Client data) {
        Object result;
        switch (column) {
            case 0:
                result = data.getFirstName();
                break;
            case 1:
                result = data.getLastName();
                break;
            case 2:
                result = data.getSms();
                break;
            case 3:
                result = data.getEmail();
                break;
            default:
                throw new IllegalStateException("This should never be executed... but it was? "+column);
        }
        return result;
    }
}
