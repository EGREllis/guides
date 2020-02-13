package net.guides.view;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ListTableModel<T> extends AbstractTableModel {
    private final String[] headers;
    private final ListTableColumnMapper<T> rowMapper;
    private final List<T> data;


    public ListTableModel(String[] headers, List<T> data, ListTableColumnMapper<T> mapper) {
        this.headers = headers;
        this.data = data;
        this.rowMapper = mapper;
    }

    @Override
    public String getColumnName(int c) {
        return headers[c];
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowMapper.getValueAt(columnIndex, data.get(rowIndex));
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
