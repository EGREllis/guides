package net.guides.view.entity;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.view.Constants;
import net.guides.view.Detail;

import javax.swing.*;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class PaymentDetail implements Detail<Payment> {
    private static final String PAYMENT_DETAIL_WINDOW_TITLE_KEY = "payment.detail.window.title";
    private static final String PAYMENT_DETAIL_CLIENT_KEY = "payment.detail.client";
    private static final String PAYMENT_DETAIL_EVENT_KEY = "payment.detail.event";
    private static final String PAYMENT_DETAIL_PAYMENT_TYPE_KEY = "payment.detail.payment.type";
    private static final String PAYMENT_DETAIL_DATE_KEY = "payment.detail.payment.date";
    private static final String PAYMENT_DETAIL_ADD_BUTTON_KEY = "payment.detail.add.button";
    private static final String PAYMENT_DETAIL_EDIT_BUTTON_KEY = "payment.detail.edit.button";
    private static final String PAYMENT_DETAIL_CANCEL_BUTTON_KEY = "payment.detail.cancel.button";
    private static final String PAYMENT_DETAIL_DATE_FORMAT_KEY = "yyyyMMdd";
    private final DateFormat dateFormat;
    private final DataAccessFacade dataAccessFacade;
    private final JFrame detailWindow;
    private final JComboBox<Client> clientJComboBox;
    private final String clientLabel;
    private final JComboBox<Event> eventJComboBox;
    private final String eventLabel;
    private final JTextField paymentDate;
    private final String paymentDateLabel;
    private final JComboBox<PaymentType> paymentTypeJComboBox;
    private final String paymentTypeLabel;
    private final String addButtonLabel;
    private final String editButtonLabel;
    private final String cancelButtonLabel;
    private final JButton proceedButton;
    private final JButton cancelButton;
    private List<Client> clientList;
    private List<Event> eventList;
    private List<PaymentType> paymentTypeList;
    private final JTextField eventIdText;
    private final JTextField clientIdText;
    private final JTextField paymentTypeIdText;

    public PaymentDetail(Properties properties, DataAccessFacade dataAccessFacade) {
        this.dataAccessFacade = dataAccessFacade;
        this.dateFormat = new SimpleDateFormat(properties.getProperty(PAYMENT_DETAIL_DATE_FORMAT_KEY));
        detailWindow = new JFrame(properties.getProperty(PAYMENT_DETAIL_WINDOW_TITLE_KEY));
        detailWindow.setVisible(false);
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setLayout(new GridLayout());

        addButtonLabel = properties.getProperty(PAYMENT_DETAIL_ADD_BUTTON_KEY);
        editButtonLabel = properties.getProperty(PAYMENT_DETAIL_EDIT_BUTTON_KEY);
        cancelButtonLabel = properties.getProperty(PAYMENT_DETAIL_CANCEL_BUTTON_KEY);
        clientLabel = properties.getProperty(PAYMENT_DETAIL_CLIENT_KEY);
        eventLabel = properties.getProperty(PAYMENT_DETAIL_EVENT_KEY);
        paymentDateLabel = properties.getProperty(PAYMENT_DETAIL_DATE_KEY);
        paymentTypeLabel = properties.getProperty(PAYMENT_DETAIL_PAYMENT_TYPE_KEY);

        detailWindow.add(new JLabel(eventLabel));
        eventJComboBox = new JComboBox<>();
        eventIdText = new JTextField();
        detailWindow.add(eventIdText);

        detailWindow.add(new JLabel(clientLabel));
        clientJComboBox = new JComboBox<>();
        clientIdText = new JTextField();
        detailWindow.add(clientIdText);

        detailWindow.add(new JLabel(paymentDateLabel));
        paymentDate = new JTextField();
        detailWindow.add(paymentDate);

        detailWindow.add(new JLabel(paymentTypeLabel));
        paymentTypeJComboBox = new JComboBox<>();
        paymentTypeIdText = new JTextField();
        detailWindow.add(paymentTypeIdText);

        proceedButton = new JButton();
        proceedButton.setText(addButtonLabel);
        detailWindow.add(proceedButton);

        cancelButton = new JButton();
        cancelButton.setText(cancelButtonLabel);
        detailWindow.add(cancelButton);

        detailWindow.pack();
    }

    @Override
    public void presentAddRecord() {
        detailWindow.setVisible(false);
        clientList = dataAccessFacade.getAllClients();
        eventList = dataAccessFacade.getAllEvents();
        paymentTypeList = dataAccessFacade.getAllPaymentTypes();
        clientIdText.setText(Constants.BLANK);
        eventIdText.setText(Constants.BLANK);
        paymentTypeIdText.setText(Constants.BLANK);
        detailWindow.setVisible(true);
    }

    @Override
    public void presentEditRecord(Payment record) {
        detailWindow.setVisible(false);
        clientIdText.setText(Integer.toString(record.getClientId()));
        eventIdText.setText(Integer.toString(record.getEventId()));
        paymentTypeIdText.setText(Integer.toString(record.getPaymentTypeId()));
        paymentDate.setText(dateFormat.format(record.getPaymentDate()));
        detailWindow.setVisible(true);
    }
}
