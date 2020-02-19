package net.guides.view;

import net.guides.data.DataAccessFacade;
import net.guides.view.loader.ClientLoader;
import net.guides.view.loader.EventLoader;
import net.guides.view.loader.PaymentLoader;
import net.guides.view.loader.PaymentTypeLoader;
import net.guides.view.table.ClientColumnMapper;
import net.guides.view.table.EventColumnMapper;
import net.guides.view.table.PaymentColumnMapper;
import net.guides.view.table.PaymentTypeColumnMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ListTableView {
    private JTabbedPane tabbedPane;
    private java.util.List<JScrollPane> scrollPanes;
    private java.util.List<JTable> tables;

    public ListTableView(DataAccessFacade dataAccessFacade) {
        tabbedPane = new JTabbedPane();
        scrollPanes = new ArrayList<>();
        tables = new ArrayList<>();

        JTable clientTable = new JTable(new ListTableModel<>(
                new ClientLoader(dataAccessFacade),
                new ClientColumnMapper()));
        JTable eventTable = new JTable(new ListTableModel<>(
                new EventLoader(dataAccessFacade),
                new EventColumnMapper()));
        JTable paymentTable = new JTable(new ListTableModel<>(
                new PaymentLoader(dataAccessFacade),
                new PaymentColumnMapper()));
        JTable paymentTypeTable = new JTable(new ListTableModel<>(
                new PaymentTypeLoader(dataAccessFacade),
                new PaymentTypeColumnMapper()));

        tables.add(clientTable);
        JScrollPane clientScrollPane = new JScrollPane(clientTable);
        scrollPanes.add(clientScrollPane);
        tabbedPane.addTab("Clients", clientScrollPane);

        tables.add(eventTable);
        JScrollPane eventScrollPane = new JScrollPane(eventTable);
        scrollPanes.add(eventScrollPane);
        tabbedPane.addTab("Events", eventScrollPane);

        tables.add(paymentTypeTable);
        JScrollPane paymentTypeScrollPane = new JScrollPane(paymentTypeTable);
        scrollPanes.add(paymentTypeScrollPane);
        tabbedPane.addTab("Payment Types", paymentTypeScrollPane);

        tables.add(paymentTable);
        JScrollPane paymentTableScrollPane = new JScrollPane(paymentTable);
        scrollPanes.add(paymentTableScrollPane);
        tabbedPane.addTab("Payment", paymentTableScrollPane);

        for (JScrollPane pane : scrollPanes) {
            pane.setPreferredSize(new Dimension(400, 100));
        }
        for (JTable table : tables) {
            table.setFillsViewportHeight(true);
        }
    }

    public void addToContainer(Container container, Object constraints) {
        container.add(tabbedPane, constraints);
    }
}
