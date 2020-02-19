package net.guides.view.tab;

import net.guides.view.*;

import javax.swing.*;
import java.awt.*;

public class TabImpl<T> implements Tab {
    private final Loader<T> loader;
    private final ColumnMapper<T> mapper;
    private final Detail<T> detail;
    private final String tabName;

    public TabImpl(String tabName, Loader<T> loader, ColumnMapper<T> mapper, Detail<T> detail) {
        this.tabName = tabName;
        this.loader = loader;
        this.mapper = mapper;
        this.detail = detail;
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
