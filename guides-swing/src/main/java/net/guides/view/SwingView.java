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
    private Properties properties;
    private DataAccessFacade dataAccessFacade;
    private JFrame window;
    private TabbedListTableView listClientsView;
    private ClientDetail clientDetail;
    private EventDetail eventDetail;
    private PaymentTypeDetail paymentTypeDetail;

    public SwingView(Properties swingProperties, DataAccessFacade facade) {
        this.dataAccessFacade = facade;
        window = new JFrame(swingProperties.getProperty(WINDOW_TITLE_KEY));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        properties = swingProperties;
    }

    public void start() {
        window.setVisible(true);

        List<Tab> tabs = new ArrayList<Tab>();
        tabs.add(new TabImpl<>("Clients", new ClientLoader(dataAccessFacade), new ClientColumnMapper(), new ClientDetail(properties)));
        tabs.add(new TabImpl<>("Events", new EventLoader(dataAccessFacade), new EventColumnMapper(), new EventDetail(properties)));
        tabs.add(new TabImpl<>("Payment Types", new PaymentTypeLoader(dataAccessFacade), new PaymentTypeColumnMapper(), new PaymentTypeDetail(properties)));
        tabs.add(new TabImpl<>("Payments", new PaymentLoader(dataAccessFacade), new PaymentColumnMapper(), new PaymentDetail(properties, dataAccessFacade)));

        listClientsView = new TabbedListTableView(dataAccessFacade, tabs);
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        clientDetail = new ClientDetail(properties);
        eventDetail = new EventDetail(properties);
        paymentTypeDetail = new PaymentTypeDetail(properties);
        window.pack();
    }
}
