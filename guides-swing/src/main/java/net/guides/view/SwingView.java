package net.guides.view;

import net.guides.model.Event;
import net.guides.view.entity.ClientDetail;
import net.guides.view.entity.EventDetail;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

public class SwingView {
    private static final String WINDOW_TITLE_KEY = "window.title";
    private Properties properties;
    private JFrame window;
    private ListTableView listClientsView;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private ClientDetail clientDetail;
    private EventDetail eventDetail;

    public SwingView(Properties swingProperties) {
        window = new JFrame(swingProperties.getProperty(WINDOW_TITLE_KEY));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        properties = swingProperties;
    }

    public void start() {
        window.setVisible(true);
        listClientsView = new ListTableView();
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        clientDetail = new ClientDetail(properties);
        eventDetail = new EventDetail(properties);
        populateButton();
        window.add(buttonPanel, BorderLayout.SOUTH);
        window.pack();
    }

    private void populateButton() {
        buttonPanel =  new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDetail.presentAddRecord();
            }
        });
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventDetail.presentEditRecord(new Event(1, "This event tile", new Date()));
            }
        });
        removeButton = new JButton("Remove");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
    }
}
