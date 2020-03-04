package net.guides.data.jdbc;

import net.guides.model.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientJDBCReader extends JDBCReaderTemplate<Client> implements JDBCReader<Client> {
    @Override
    protected Client readResultSet(ResultSet resultSet) {
        try {
            Integer clientId = resultSet.getInt("client_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String sms = resultSet.getString("sms");
            String email = resultSet.getString("email");
            Client client = new Client(clientId, firstName, lastName, sms, email);
            return client;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }
}
