package net.guides.view;

public interface ColumnMapper<T> {
    Object getValueAt(int column, T data);
    String getColumnName(int c);
    int getColumnCount();
}
