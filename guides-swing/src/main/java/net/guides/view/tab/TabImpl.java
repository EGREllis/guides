package net.guides.view.tab;

import net.guides.controller.Command;
import net.guides.controller.Listener;
import net.guides.view.*;
import net.guides.view.components.ListTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Properties;

public class TabImpl<T> implements Tab, Listener {
    public static final String TAB_TABLE_TEXT_KEY = "%1$s.table";
    public static final String TAB_ADD_BUTTON_TEXT_KEY = "%1$s.add.button";
    public static final String TAB_EDIT_BUTTON_TEXT_KEY = "%1$s.edit.button";
    public static final String TAB_DELETE_BUTTON_TEXT_KEY = "%1$s.delete.button";
    public static final String TAB_DELETE_CONFIRM_TITLE_KEY = "%1$s.confirm.delete.title.text";
    public static final String TAB_DELETE_CONFIRM_TEXT_KEY = "%1$s.confirm.delete.text";
    public static final String TAB_DELETE_CONFIRM_PROCEED_KEY = "%1$s.confirm.delete.proceed.button";
    public static final String TAB_DELETE_CONFIRM_CANCEL_KEY = "%1$s.confirm.delete.cancel.button";
    private final Properties properties;
    private final Loader<T> loader;
    private final ColumnMapper<T> mapper;
    private final Detail<T> detail;
    private final String tabName;
    private final String prefix;
    private final Command<T> deleteCommand;
    private JPanel panel;
    private JTable table;
    private ListTableModel<T> listTableModel;
    private Map<String,Component> componentMap;

    public TabImpl(String tabName, Loader<T> loader, ColumnMapper<T> mapper, Detail<T> detail, Properties properties, String prefix, Command<T> deleteCommand, Map<String,Component> componentMap) {
        this.tabName = tabName;
        this.loader = loader;
        this.mapper = mapper;
        this.detail = detail;
        this.properties = properties;
        this.prefix = prefix;
        this.deleteCommand = deleteCommand;
        this.componentMap = componentMap;
    }

    @Override
    public Container getContainer() {
        this.panel = new JPanel();
        panel.setLayout(new BorderLayout());
        listTableModel = new ListTableModel<>(loader, mapper);
        table = new JTable(listTableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
                if (record != null) {
                    detail.presentEditRecord(record);
                }
            }
        });
        JButton deleteButton = new JButton(properties.getProperty(String.format(TAB_DELETE_BUTTON_TEXT_KEY, prefix)));
        buttonPanel.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame dialogFrame = new JFrame();
                String[] options = new String[] {
                        properties.getProperty(String.format(TAB_DELETE_CONFIRM_PROCEED_KEY, prefix)),
                        properties.getProperty(String.format(TAB_DELETE_CONFIRM_CANCEL_KEY, prefix))
                };
                int n = JOptionPane.showOptionDialog(dialogFrame,
                        properties.getProperty(String.format(TAB_DELETE_CONFIRM_TEXT_KEY, prefix)),
                        properties.getProperty(String.format(TAB_DELETE_CONFIRM_TITLE_KEY, prefix)),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);
                if (n == 0) {
                    T record = getSelectedRecord();
                    deleteCommand.execute(record);
                    alert();
                }
            }
        });
        panel.add(buttonPanel, BorderLayout.SOUTH);

        componentMap.put(String.format(TAB_ADD_BUTTON_TEXT_KEY, prefix), addButton);
        componentMap.put(String.format(TAB_EDIT_BUTTON_TEXT_KEY, prefix), editButton);
        componentMap.put(String.format(TAB_DELETE_BUTTON_TEXT_KEY, prefix), deleteButton);
        componentMap.put(String.format(TAB_TABLE_TEXT_KEY, prefix), table);

        return panel;
    }

    @Override
    public String getTabName() {
        return tabName;
    }

    private T getSelectedRecord() {
        T result = null;
        int index;
        try {
            index = table.getSelectedRow();
            if (index >= 0) {
                result = listTableModel.getList().get(index);
            }
        } catch (ArrayIndexOutOfBoundsException aiob) {
            // Nothing is selected
            return null;
        }
        return result;
    }

    @Override
    public void alert() {
        listTableModel.alert();
    }
}
