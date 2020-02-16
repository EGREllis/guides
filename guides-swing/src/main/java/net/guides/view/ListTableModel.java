package net.guides.view;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ListTableModel<T> extends AbstractTableModel {
    private final ColumnMapper<T> rowMapper;
    private final List<T> data;


    public ListTableModel(List<T> data, ColumnMapper<T> mapper) {
        this.data = data;
        this.rowMapper = mapper;
    }

    @Override
    public String getColumnName(int c) {
        return rowMapper.getColumnName(c);
    }

    @Override
    public int getColumnCount() {
        return rowMapper.getColumnCount();
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
