package net.guides.view;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.view.command.*;
import net.guides.view.detail.ClientDetail;
import net.guides.view.detail.EventDetail;
import net.guides.view.detail.PaymentDetail;
import net.guides.view.detail.PaymentTypeDetail;
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
        tabs.add(createClientTab(properties, dataAccessFacade));
        tabs.add(createEventTab(properties, dataAccessFacade));
        tabs.add(createPaymentTypeDetailTab(properties, dataAccessFacade));
        tabs.add(createPaymentDetailTab(properties, dataAccessFacade));

        listClientsView = new TabbedListTableView(dataAccessFacade, tabs);
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        window.pack();
    }

    private Tab createClientTab(Properties properties, DataAccessFacade facade) {
        Loader<Client> clientLoader = new ClientLoader(dataAccessFacade);
        ColumnMapper<Client> mapper = new ClientColumnMapper();
        FacadeCommandTemplate<Client> addCommand = new ClientAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Client> editCommand = new ClientEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Client> deleteCommand = new ClientDeleteCommand(dataAccessFacade, properties);
        Detail<Client> details = new ClientDetail(properties, facade, addCommand, editCommand, deleteCommand);
        final Tab tab = new TabImpl<>("Clients", clientLoader, mapper, details, properties, CLIENT_TAB_PREFIX);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    private Tab createEventTab(Properties properties, DataAccessFacade dataAccessFacade) {
        Loader<Event> eventLoader = new EventLoader(dataAccessFacade);
        ColumnMapper<Event> mapper = new EventColumnMapper();
        FacadeCommandTemplate<Event> addCommand = new EventAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Event> editCommand = new EventEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Event> deleteCommand = new EventDeleteCommand(dataAccessFacade, properties);
        Detail<Event> details = new EventDetail(properties, addCommand, editCommand, deleteCommand);
        final Tab tab = new TabImpl<>("Events", eventLoader, mapper, details, properties, EVENT_TAB_PREFIX);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    private Tab createPaymentTypeDetailTab(Properties properties, DataAccessFacade dataAccessFacade) {
        Loader<PaymentType> paymentTypeLoader = new PaymentTypeLoader(dataAccessFacade);
        ColumnMapper<PaymentType> mapper = new PaymentTypeColumnMapper();
        FacadeCommandTemplate<PaymentType> addCommand = new PaymentTypeAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<PaymentType> editCommand = new PaymentTypeEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<PaymentType> deleteCommand = new PaymentTypeDeleteCommand(dataAccessFacade, properties);
        Detail<PaymentType> details = new PaymentTypeDetail(properties, addCommand, editCommand, deleteCommand);
        final Tab tab = new TabImpl<>("Payment type", paymentTypeLoader, mapper, details, properties, PAYMENT_TYPE_TAB_PREFIX);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    private Tab createPaymentDetailTab(Properties properties, DataAccessFacade dataAccessFacade) {
        Loader<Payment> paymentLoader = new PaymentLoader(dataAccessFacade);
        ColumnMapper<Payment> mapper = new PaymentColumnMapper();
        FacadeCommandTemplate<Payment> addCommand = new PaymentAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Payment> editCommand = new PaymentEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Payment> deleteCommand = new PaymentDeleteCommand(dataAccessFacade, properties);
        Detail<Payment> detail = new PaymentDetail(properties, dataAccessFacade, addCommand, editCommand, deleteCommand);
        final Tab tab = new TabImpl<>("Payment", paymentLoader, mapper, detail, properties, PAYMENT_TAB_PREFIX);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }
}
