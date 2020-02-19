package net.guides.view.tab;

import net.guides.view.ColumnMapper;
import net.guides.view.ListTableModel;
import net.guides.view.Loader;
import net.guides.view.Tab;

import javax.swing.*;
import java.awt.*;

public class TabImpl<T> implements Tab {
    private final Loader<T> loader;
    private final ColumnMapper<T> mapper;
    private final String tabName;

    public TabImpl(String tabName, Loader<T> loader, ColumnMapper<T> mapper) {
        this.tabName = tabName;
        this.loader = loader;
        this.mapper = mapper;
    }

    @Override
    public Container getContainer() {
        JTable table = new JTable(new ListTableModel<>(loader, mapper));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        table.setFillsViewportHeight(true);
        return scrollPane;
    }

    @Override
    public String getTabName() {
        return tabName;
    }
}
