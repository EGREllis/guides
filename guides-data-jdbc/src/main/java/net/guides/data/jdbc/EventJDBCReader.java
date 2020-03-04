package net.guides.data.jdbc;

import net.guides.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class EventJDBCReader extends JDBCReaderTemplate<Event> implements JDBCReader<Event> {
    @Override
    protected Event readResultSet(ResultSet resultSet) {
        try {
            Integer eventId = resultSet.getInt("event_id");
            String title = resultSet.getString("title");
            Date startDate = resultSet.getDate("start_date");
            Event event = new Event(eventId, title, startDate);
            return event;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }
}
