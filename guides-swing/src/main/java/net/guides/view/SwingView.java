package net.guides.view;

import net.guides.controller.Listener;
import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.controller.command.*;
import net.guides.view.components.LoaderDrivenComboBox;
import net.guides.view.detail.ClientDetail;
import net.guides.view.detail.EventDetail;
import net.guides.view.detail.PaymentDetail;
import net.guides.view.detail.PaymentTypeDetail;
import net.guides.view.format.ClientFormatter;
import net.guides.view.format.EventFormatter;
import net.guides.view.format.PaymentTypeFormatter;
import net.guides.view.loader.ClientLoader;
import net.guides.view.loader.EventLoader;
import net.guides.view.loader.PaymentLoader;
import net.guides.view.loader.PaymentTypeLoader;
import net.guides.view.tab.TabImpl;
import net.guides.view.mapper.ClientColumnMapper;
import net.guides.view.mapper.EventColumnMapper;
import net.guides.view.mapper.PaymentColumnMapper;
import net.guides.view.mapper.PaymentTypeColumnMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;
import java.util.List;

public class SwingView {
    public static final String TAB_TITLE_KEY = "%1$s.title";
    public static final String WINDOW_TITLE_KEY = "window.title";
    public static final String CLIENT_TAB_PREFIX = "client.tab";
    public static final String EVENT_TAB_PREFIX = "event.tab";
    public static final String PAYMENT_TYPE_TAB_PREFIX = "payment.type.tab";
    public static final String PAYMENT_TAB_PREFIX = "payment.tab";

    private Properties properties;
    private DataAccessFacade dataAccessFacade;
    private JFrame window;
    private TabbedListTableView listClientsView;
    private List<Listener> listeners;
    private Map<String, Component> components = new HashMap<>();

    private SwingView(Properties swingProperties, DataAccessFacade facade, int defaultCloseOperation) {
        this.dataAccessFacade = facade;
        window = new JFrame(swingProperties.getProperty(WINDOW_TITLE_KEY));
        window.setDefaultCloseOperation(defaultCloseOperation);
        window.setLayout(new BorderLayout());
        properties = swingProperties;
        listeners = new ArrayList<>();
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Window closing...");
                for (Listener listener : listeners) {
                    listener.alert();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public static SwingView newSwingView(Properties properties, DataAccessFacade dataAccessFacade) {
        return new SwingView(properties, dataAccessFacade, WindowConstants.EXIT_ON_CLOSE);
    }

    public static SwingView newTestSwingView(Properties properties, DataAccessFacade dataAccessFacade) {
        return new SwingView(properties, dataAccessFacade, WindowConstants.DISPOSE_ON_CLOSE);
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void start() {
        window.setVisible(true);

        List<Tab> tabs = new ArrayList<>();
        tabs.add(createClientTab(CLIENT_TAB_PREFIX));
        tabs.add(createEventTab(EVENT_TAB_PREFIX));
        tabs.add(createPaymentTypeDetailTab(PAYMENT_TYPE_TAB_PREFIX));
        tabs.add(createPaymentDetailTab(PAYMENT_TAB_PREFIX));

        listClientsView = new TabbedListTableView(tabs);
        listClientsView.addToContainer(window, BorderLayout.CENTER);
        window.pack();
    }

    private Tab createClientTab(String prefix) {
        Loader<Client> clientLoader = new ClientLoader(dataAccessFacade);
        ColumnMapper<Client> mapper = new ClientColumnMapper();
        FacadeCommandTemplate<Client> addCommand = new ClientAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Client> editCommand = new ClientEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Client> deleteCommand = new ClientDeleteCommand(dataAccessFacade, properties);
        Detail<Client> details = new ClientDetail(properties, addCommand, editCommand, components);
        String tabTitle = properties.getProperty(String.format(TAB_TITLE_KEY, prefix));
        final Tab tab = new TabImpl<>(tabTitle, clientLoader, mapper, details, properties, prefix, deleteCommand, components);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    private Tab createEventTab(String prefix) {
        Loader<Event> eventLoader = new EventLoader(dataAccessFacade);
        ColumnMapper<Event> mapper = new EventColumnMapper();
        FacadeCommandTemplate<Event> addCommand = new EventAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Event> editCommand = new EventEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Event> deleteCommand = new EventDeleteCommand(dataAccessFacade, properties);
        Detail<Event> details = new EventDetail(properties, addCommand, editCommand);
        String tabTitle = properties.getProperty(String.format(TAB_TITLE_KEY, prefix));
        final Tab tab = new TabImpl<>(tabTitle, eventLoader, mapper, details, properties, prefix, deleteCommand, components);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    private Tab createPaymentTypeDetailTab(String prefix) {
        Loader<PaymentType> paymentTypeLoader = new PaymentTypeLoader(dataAccessFacade);
        ColumnMapper<PaymentType> mapper = new PaymentTypeColumnMapper();
        FacadeCommandTemplate<PaymentType> addCommand = new PaymentTypeAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<PaymentType> editCommand = new PaymentTypeEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<PaymentType> deleteCommand = new PaymentTypeDeleteCommand(dataAccessFacade, properties);
        Detail<PaymentType> details = new PaymentTypeDetail(properties, addCommand, editCommand);
        String tabTitle = properties.getProperty(String.format(TAB_TITLE_KEY, prefix));
        final Tab tab = new TabImpl<>(tabTitle, paymentTypeLoader, mapper, details, properties, prefix, deleteCommand, components);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    private Tab createPaymentDetailTab(String prefix) {
        Loader<Payment> paymentLoader = new PaymentLoader(dataAccessFacade);
        ColumnMapper<Payment> mapper = new PaymentColumnMapper();
        FacadeCommandTemplate<Payment> addCommand = new PaymentAddCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Payment> editCommand = new PaymentEditCommand(dataAccessFacade, properties);
        FacadeCommandTemplate<Payment> deleteCommand = new PaymentDeleteCommand(dataAccessFacade, properties);

        LoaderDrivenComboBox<Client> clientBox = new LoaderDrivenComboBox<>(new JComboBox<String>(), new ClientLoader(dataAccessFacade), new ClientFormatter(properties));
        LoaderDrivenComboBox<Event> eventBox = new LoaderDrivenComboBox<>(new JComboBox<String>(), new EventLoader(dataAccessFacade), new EventFormatter(properties));
        LoaderDrivenComboBox<PaymentType> paymentTypeBox = new LoaderDrivenComboBox<>(new JComboBox<String>(), new PaymentTypeLoader(dataAccessFacade), new PaymentTypeFormatter(properties));

        Detail<Payment> detail = new PaymentDetail(dataAccessFacade, properties, addCommand, editCommand, clientBox, eventBox, paymentTypeBox);
        String tabTitle = properties.getProperty(String.format(TAB_TITLE_KEY, prefix));
        final Tab tab = new TabImpl<>(tabTitle, paymentLoader, mapper, detail, properties, prefix, deleteCommand, components);
        addCommand.addListener(tab);
        editCommand.addListener(tab);
        return tab;
    }

    public Map<String,Component> getComponents() {
        return Collections.unmodifiableMap(components);
    }

    public JFrame getWindow() {
        return window;
    }

    public void stop() {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }
}
