package net.guides.view.table;

import net.guides.model.Event;
import net.guides.view.ColumnMapper;

public class EventColumnMapper implements ColumnMapper<Event> {
    private static final String[] columnNames = new String[] {"Description", "Start date"};

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int c) {
        return columnNames[c];
    }

    @Override
    public Object getValueAt(int column, Event data) {
        Object result;
        switch (column) {
            case 0:
                result = data.getTitle();
                break;
            case 1:
                result = data.getStartDate();
                break;
            default:
                throw new IllegalStateException("This should never be called... but it was: "+column);
        }
        return result;
    }
}
