package net.guides.view;

import net.guides.data.DataAccessFacade;
import net.guides.view.entity.ClientDetail;
import net.guides.view.entity.EventDetail;
import net.guides.view.entity.PaymentDetail;
import net.guides.view.entity.PaymentTypeDetail;
import net.guides.view.loader.ClientLoader;
import net.guides.view.loader.EventLoader;
import net.guides.view.loader.PaymentLoader;
import net.guides.view.loader.PaymentTypeLoader;
import net.guides.view.tab.TabImpl;
import net.guides.view.table.ClientColumnMapper;
import net.guides.view.table.EventColumnMapper;
import net.guides.view.table.PaymentColumnMapper;
import net.guides.view.table.PaymentTypeColumnMapper;

import javax.swing.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SwingView {
    private static final String WINDOW_TITLE_KEY = "window.title";
    private static final String CLIENT_TAB_PREFIX = "client.tab";
    private static final String EVENT_TAB_PREFIX = "event.tab";
    private static final String PAYMENT_TYPE_TAB_PREFIX = "payment.type.tab";
    private static final String PAYMENT_TAB_PREFIX = "payment.tab";

    private Properties properties;
    private DataAccessFacade dataAccessFacade;
    private JFrame window;
    private TabbedListTableView listClientsView;

    public SwingView(Properties swingProperties, DataAccessFacade facade) {
        this.dataAccessFacade = facade;
        window = new JFrame(swingProperties.getProperty(WINDOW_TITLE_KEY));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        properties = swingProperties;
    }

    public void start() {
        window.setVisible(true);

        List<Tab> tabs = new ArrayList<>();
        tabs.add(new TabImpl<>("Clients", new ClientLoader(dataAccessFacade), new ClientColumnMapper(), new ClientDetail(properties), properties, CLIENT_TAB_PREFIX));
        tabs.add(new TabImpl<>("Events", new EventLoader(dataAccessFacade), new EventColumnMapper(), new EventDetail(properties), properties, EVENT_TAB_PREFIX));
        tabs.add(new TabImpl<>("Payment Types", new PaymentTypeLoader(dataAccessFacade), new PaymentTypeColumnMapper(), new PaymentTypeDetail(properties), properties, PAYMENT_TYPE_TAB_PREFIX));
        tabs.add(new TabImpl<>("Payments", new PaymentLoader(dataAccessFacade), new PaymentColumnMapper(), new PaymentDetail(properties, dataAccessFacade), properties, PAYMENT_TAB_PREFIX));

        listClientsView = new TabbedListTableView(dataAccessFacade, tabs);
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        window.pack();
    }
}
