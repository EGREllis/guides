package net.guides.view;

import net.guides.model.Client;
import net.guides.view.entity.ClientDetail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        clientDetail.pack();
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
                clientDetail.presentAddRecord();
            }
        });
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientDetail.presentEditRecord(new Client(1, "DummyFirstName", "DummyLastName", "DummySms", "DummyEmail"));
            }
        });
        removeButton = new JButton("Remove");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
    }
}
