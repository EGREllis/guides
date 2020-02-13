package net.guides.view;

import net.guides.model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class ListTableView {
    private JScrollPane myScrollPane;
    private JTable myTable;

    public ListTableView() {
        myTable = new JTable(new ListTableModel<Client>(new String[]{"First name", "Last name", "SMS", "Email"},
                Arrays.asList(new Client(1, "Gary", "Blower", "07853000000", "a@b")),
        new ClientListTableRowMapper()));
        myScrollPane = new JScrollPane(myTable);
        myScrollPane.setPreferredSize(new Dimension(400, 100));
        myTable.setFillsViewportHeight(true);
    }

    public void addToContainer(Container container, Object constraints) {
        container.add(myScrollPane, constraints);
    }
}
