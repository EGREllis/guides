package net.guides.view;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class SwingView {
    private static final String WINDOW_TITLE_KEY = "window.title";
    private JFrame window;

    public SwingView(Properties swingProperties) {
        window = new JFrame(swingProperties.getProperty(WINDOW_TITLE_KEY));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
    }

    public void start() {
        window.setVisible(true);
        ListTableView listClientsView = new ListTableView();
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        window.pack();
    }
}
