package net.guides.view.detail;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import net.guides.controller.Command;
import net.guides.view.Constants;
import net.guides.view.Detail;
import net.guides.view.components.LoaderDrivenComboBox;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final DataAccessFacade dataAccessFacade;
    private final DateFormat dateFormat;
    private final JFrame detailWindow;
    private final LoaderDrivenComboBox<Client> clientBox;
    private final LoaderDrivenComboBox<Event> eventBox;
    private final LoaderDrivenComboBox<PaymentType> paymentTypeBox;
    private final JTextField paymentDate;
    private final String addButtonLabel;
    private final String editButtonLabel;
    private final JButton proceedButton;
    private Integer id;
    private final ActionListener addListener;
    private final ActionListener editListener;

    public PaymentDetail(DataAccessFacade dataAccessFacade, Properties properties, final Command<Payment> addCommand, final Command<Payment> editCommand, final Command<Payment> deleteCommand, LoaderDrivenComboBox<Client> clientBox, LoaderDrivenComboBox<Event> eventBox, LoaderDrivenComboBox<PaymentType> paymentTypeBox) {
        this.dataAccessFacade = dataAccessFacade;
        this.dateFormat = new SimpleDateFormat(properties.getProperty(PAYMENT_DETAIL_DATE_FORMAT_KEY));
        this.clientBox = clientBox;
        this.eventBox = eventBox;
        this.paymentTypeBox = paymentTypeBox;
        detailWindow = new JFrame(properties.getProperty(PAYMENT_DETAIL_WINDOW_TITLE_KEY));
        detailWindow.setVisible(false);
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setLayout(new GridLayout(5,2));

        addButtonLabel = properties.getProperty(PAYMENT_DETAIL_ADD_BUTTON_KEY);
        editButtonLabel = properties.getProperty(PAYMENT_DETAIL_EDIT_BUTTON_KEY);
        String cancelButtonLabel = properties.getProperty(PAYMENT_DETAIL_CANCEL_BUTTON_KEY);

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_CLIENT_KEY)));
        detailWindow.add(clientBox.getComboBox());

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_EVENT_KEY)));
        detailWindow.add(eventBox.getComboBox());

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_PAYMENT_TYPE_KEY)));
        detailWindow.add(paymentTypeBox.getComboBox());

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_DETAIL_DATE_KEY)));
        paymentDate = new JTextField();
        detailWindow.add(paymentDate);

        proceedButton = new JButton();
        proceedButton.setText(addButtonLabel);
        detailWindow.add(proceedButton);

        JButton cancelButton = new JButton();
        cancelButton.setText(cancelButtonLabel);
        detailWindow.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailWindow.setVisible(false);
            }
        });

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
        int clientId = clientBox.getSelectedItem().getClientId();
        int eventId = eventBox.getSelectedItem().getEventId();
        int paymentTypeId = paymentTypeBox.getSelectedItem().getId();
        Client client = dataAccessFacade.getClient(clientId);
        Event event = dataAccessFacade.getEvent(eventId);
        PaymentType paymentType = dataAccessFacade.getPaymentType(paymentTypeId);
        Date paymentDateValue;
        try {
            paymentDateValue = dateFormat.parse(paymentDate.getText());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Payment(id, client, event, paymentType, paymentDateValue);
    }

    @Override
    public void presentAddRecord() {
        detailWindow.setVisible(false);
        paymentDate.setText(Constants.BLANK);
        proceedButton.setText(addButtonLabel);
        proceedButton.addActionListener(addListener);
        proceedButton.removeActionListener(editListener);
        detailWindow.setVisible(true);
    }

    @Override
    public void presentEditRecord(Payment record) {
        detailWindow.setVisible(false);
        paymentDate.setText(dateFormat.format(record.getPaymentDate()));
        proceedButton.setText(editButtonLabel);
        proceedButton.addActionListener(editListener);
        proceedButton.removeActionListener(addListener);
        detailWindow.setVisible(true);
        id = record.getId();
    }
}
