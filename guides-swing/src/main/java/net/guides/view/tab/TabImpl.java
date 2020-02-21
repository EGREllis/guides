package net.guides.view.tab;

import net.guides.view.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

public class TabImpl<T> implements Tab, Listener {
    private static final String TAB_ADD_BUTTON_TEXT_KEY = "%1$s.add.button";
    private static final String TAB_EDIT_BUTTON_TEXT_KEY = "%1$s.edit.button";
    private static final String TAB_DELETE_BUTTON_TEXT_KEY = "%1$s.delete.button";
    private final Properties properties;
    private final Loader<T> loader;
    private final ColumnMapper<T> mapper;
    private final Detail<T> detail;
    private final String tabName;
    private final String prefix;
    private JPanel panel;
    private JTable table;
    private ListTableModel<T> listTableModel;

    public TabImpl(String tabName, Loader<T> loader, ColumnMapper<T> mapper, Detail<T> detail, Properties properties, String prefix) {
        this.tabName = tabName;
        this.loader = loader;
        this.mapper = mapper;
        this.detail = detail;
        this.properties = properties;
        this.prefix = prefix;
    }

    @Override
    public Container getContainer() {
        this.panel = new JPanel();
        panel.setLayout(new BorderLayout());
        listTableModel = new ListTableModel<>(loader, mapper);
        table = new JTable(listTableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 100));
        table.setFillsViewportHeight(true);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3));
        JButton addButton = new JButton(properties.getProperty(String.format(TAB_ADD_BUTTON_TEXT_KEY, prefix)));
        buttonPanel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detail.presentAddRecord();
            }
        });
        JButton editButton = new JButton(properties.getProperty(String.format(TAB_EDIT_BUTTON_TEXT_KEY, prefix)));
        buttonPanel.add(editButton);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                T record = getSelectedRecord();
                detail.presentEditRecord(record);
            }
        });
        JButton deleteButton = new JButton(properties.getProperty(String.format(TAB_DELETE_BUTTON_TEXT_KEY, prefix)));
        buttonPanel.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Confirm?
            }
        });
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    private T getSelectedRecord() {
        return listTableModel.getList().get(table.getSelectedRow());
    }

    @Override
    public void alert() {
        listTableModel.alert();
    }
}
