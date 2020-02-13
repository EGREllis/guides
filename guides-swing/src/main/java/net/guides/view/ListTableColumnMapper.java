package net.guides.view;

public interface ListTableColumnMapper<T> {
    Object getValueAt(int column, T data);
}
