package net.guides.data.jdbc;

import net.guides.model.Identifiable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class JDBCReaderTemplate<T extends Identifiable> implements JDBCReader<T>{
    protected abstract T readResultSet(ResultSet resultSet);

    public Map<Integer,T> getResultsMap(ResultSet resultSet) {
        Map<Integer, T> resultsMap = new HashMap<>();
        try {
            while ( (resultSet.next()) ) {
                T result = readResultSet(resultSet);
                resultsMap.put(result.getId(), result);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return resultsMap;
    }
}
