package net.guides.data.jdbc;

import net.guides.model.Identifiable;

import java.sql.ResultSet;
import java.util.Map;

public interface JDBCReader<T extends Identifiable> {
    Map<Integer,T> getResultsMap(ResultSet resultSet);
}
