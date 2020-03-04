package net.guides.data.jdbc;

import net.guides.model.PaymentType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentTypeJDBCReader extends JDBCReaderTemplate<PaymentType> implements JDBCReader<PaymentType> {
    @Override
    protected PaymentType readResultSet(ResultSet resultSet) {
        try {
            Integer id = resultSet.getInt("payment_type_id");
            String description = resultSet.getString("description");
            PaymentType paymentType = new PaymentType(id, description);
            return paymentType;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }
}
