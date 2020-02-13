package net.guides.view;

import net.guides.model.Client;

public class ClientListTableRowMapper implements ListTableColumnMapper<Client> {
    @Override
    public Object getValueAt(int column, Client data) {
        Object result = null;
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
