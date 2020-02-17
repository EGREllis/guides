package net.guides.view;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class SwingView {
    private static final String WINDOW_TITLE_KEY = "window.title";
    private JFrame window;
    private ListTableView listClientsView;
    private JPanel buttonPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    public SwingView(Properties swingProperties) {
        window = new JFrame(swingProperties.getProperty(WINDOW_TITLE_KEY));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }

    public void start() {
        window.setVisible(true);
        listClientsView = new ListTableView();
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        populateButton();
        window.add(buttonPanel, BorderLayout.SOUTH);
        window.pack();
    }

    private void populateButton() {
        buttonPanel =  new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        removeButton = new JButton("Remove");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);
    }
}
