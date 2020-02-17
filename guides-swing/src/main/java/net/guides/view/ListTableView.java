package net.guides.view;

import net.guides.model.Client;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.view.table.ClientColumnMapper;
import net.guides.view.table.EventColumnMapper;
import net.guides.view.table.PaymentColumnMapper;
import net.guides.view.table.PaymentTypeColumnMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ListTableView {
    private JTabbedPane tabbedPane;
    private java.util.List<JScrollPane> scrollPanes;
    private java.util.List<JTable> tables;

    public ListTableView() {
        tabbedPane = new JTabbedPane();
        scrollPanes = new ArrayList<>();
        tables = new ArrayList<>();

        JTable clientTable = new JTable(new ListTableModel<Client>(
                Arrays.asList(new Client(1, "Gary", "Blower", "07853000000", "a@b")),
                new ClientColumnMapper()));
        JTable eventTable = new JTable(new ListTableModel<net.guides.model.Event>(
                Arrays.asList(new net.guides.model.Event(1, "Test date", new Date())),
                new EventColumnMapper()));
        JTable paymentTable = new JTable(new ListTableModel<Payment>(
                Arrays.asList(new Payment(0, 0, 0, PaymentType.CARD, new Date())),
                new PaymentColumnMapper()));
        JTable paymentTypeTable = new JTable(new ListTableModel<PaymentType>(
                Arrays.asList(PaymentType.CASH, PaymentType.CARD),
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
