package net.guides.view.detail;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.controller.Command;
import net.guides.view.Constants;
import net.guides.view.Detail;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private static final String PAYMENT_DETAIL_DATE_FORMAT_KEY = "date.format";
    private final DateFormat dateFormat;
    private final DataAccessFacade dataAccessFacade;
    private final JFrame detailWindow;
    private final JComboBox<Client> clientJComboBox;
    private final JComboBox<Event> eventJComboBox;
    private final JTextField paymentDate;
    private final JComboBox<PaymentType> paymentTypeJComboBox;
    private final String addButtonLabel;
    private final String editButtonLabel;
    private final String cancelButtonLabel;
    private final JButton proceedButton;
    private final JButton cancelButton;
    private List<Client> clientList;
    private List<Event> eventList;
    private List<PaymentType> paymentTypeList;
    private Integer id;
    private final JTextField eventIdText;
    private final JTextField clientIdText;
    private final JTextField paymentTypeIdText;
    private final Command<Payment> addCommand;
    private final Command<Payment> editCommand;
    private final Command<Payment> deleteCommand;
    private final ActionListener addListener;
    private final ActionListener editListener;

    public PaymentDetail(Properties properties, DataAccessFacade dataAccessFacade, final Command<Payment> addCommand, final Command<Payment> editCommand, final Command<Payment> deleteCommand) {
        this.dataAccessFacade = dataAccessFacade;
        this.dateFormat = new SimpleDateFormat(properties.getProperty(PAYMENT_DETAIL_DATE_FORMAT_KEY));
        detailWindow = new JFrame(properties.getProperty(PAYMENT_DETAIL_WINDOW_TITLE_KEY));
        detailWindow.setVisible(false);
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setLayout(new GridLayout(5,2));

        addButtonLabel = properties.getProperty(PAYMENT_DETAIL_ADD_BUTTON_KEY);
        editButtonLabel = properties.getProperty(PAYMENT_DETAIL_EDIT_BUTTON_KEY);
        cancelButtonLabel = properties.getProperty(PAYMENT_DETAIL_CANCEL_BUTTON_KEY);

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_CLIENT_KEY)));
        clientJComboBox = new JComboBox<>();
        clientIdText = new JTextField();
        detailWindow.add(clientIdText);

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_EVENT_KEY)));
        eventJComboBox = new JComboBox<>();
        eventIdText = new JTextField();
        detailWindow.add(eventIdText);

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_DATE_KEY)));
        paymentDate = new JTextField();
        detailWindow.add(paymentDate);

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_PAYMENT_TYPE_KEY)));
        paymentTypeJComboBox = new JComboBox<>();
        paymentTypeIdText = new JTextField();
        detailWindow.add(paymentTypeIdText);

        proceedButton = new JButton();
        proceedButton.setText(addButtonLabel);
        detailWindow.add(proceedButton);

        cancelButton = new JButton();
        cancelButton.setText(cancelButtonLabel);
        detailWindow.add(cancelButton);

        this.addCommand = addCommand;
        this.editCommand = editCommand;
        this.deleteCommand = deleteCommand;
        this.addListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Payment payment = getRecord();
                addCommand.execute(payment);
                detailWindow.setVisible(false);
            }
        };
        this.editListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Payment payment = getRecord();
                editCommand.execute(payment);
                detailWindow.setVisible(false);
            }
        };

        detailWindow.pack();
    }

    private Payment getRecord() {
        int clientId = Integer.parseInt(clientIdText.getText());
        int eventId = Integer.parseInt(eventIdText.getText());
        int paymentTypeId = Integer.parseInt(paymentTypeIdText.getText());
        Date paymentDateValue;
        try {
            paymentDateValue = dateFormat.parse(paymentDate.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Payment(id, clientId, eventId, paymentTypeId, paymentDateValue);
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
        paymentDate.setText(Constants.BLANK);
        proceedButton.setText(addButtonLabel);
        proceedButton.addActionListener(addListener);
        proceedButton.removeActionListener(editListener);
        detailWindow.setVisible(true);
    }

    @Override
    public void presentEditRecord(Payment record) {
        detailWindow.setVisible(false);
        clientIdText.setText(Integer.toString(record.getClientId()));
        eventIdText.setText(Integer.toString(record.getEventId()));
        paymentTypeIdText.setText(Integer.toString(record.getPaymentTypeId()));
        paymentDate.setText(dateFormat.format(record.getPaymentDate()));
        proceedButton.setText(editButtonLabel);
        proceedButton.addActionListener(editListener);
        proceedButton.removeActionListener(addListener);
        detailWindow.setVisible(true);
    }
}
