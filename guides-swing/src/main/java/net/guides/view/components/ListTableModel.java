package net.guides.view.components;

import net.guides.controller.Listener;
import net.guides.view.ColumnMapper;
import net.guides.view.Loader;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ListTableModel<T> extends AbstractTableModel implements Listener {
    private final ColumnMapper<T> rowMapper;
    private final Loader<T> loader;
    private List<T> data;


    public ListTableModel(Loader<T> loader, ColumnMapper<T> mapper) {
        this.rowMapper = mapper;
        this.loader = loader;
        data = loader.load();
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

    public List<T> getList() {
        return data;
    }

    @Override
    public void alert() {
        data = loader.load();
        this.fireTableDataChanged();
    }
}
