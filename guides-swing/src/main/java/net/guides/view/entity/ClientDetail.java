package net.guides.view.entity;

import net.guides.model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class ClientDetail {
    private static final String CLIENT_DETAIL_ADD_TITLE_KEY = "client.add.title";
    private static final String CLIENT_DETAIL_FIRST_NAME_KEY = "client.detail.first.name";
    private static final String CLIENT_DETAIL_LAST_NAME_KEY = "client.detail.last.name";
    private static final String CLIENT_DETAIL_SMS_KEY = "client.detail.sms";
    private static final String CLIENT_DETAIL_EMAIL_KEY = "client.detail.email";
    private static final String CLIENT_DETAIL_ADD_BUTTON_KEY = "client.detail.add.button.key";
    private static final String CLIENT_DETAIL_EDIT_BUTTON_KEY = "client.detail.edit.button.key";
    private static final String CLIENT_DETAIL_CANCEL_BUTTON_KEY = "client.detail.cancel.button.key";
    private static final String BLANK = "";
    private final String labelFirstName;
    private final String labelLastName;
    private final String labelSms;
    private final String labelEmail;
    private final String labelAddButton;
    private final String labelEditButton;
    private final String labelCancelButton;
    private JFrame detailWindow;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField sms;
    private JTextField email;
    private JButton proceedButton;
    private JButton cancelButton;

    public ClientDetail(Properties properties) {
        detailWindow = new JFrame(properties.getProperty(CLIENT_DETAIL_ADD_TITLE_KEY));
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setVisible(false);
        detailWindow.setLayout(new GridLayout(5, 2));
        labelFirstName = properties.getProperty(CLIENT_DETAIL_FIRST_NAME_KEY);
        labelLastName = properties.getProperty(CLIENT_DETAIL_LAST_NAME_KEY);
        labelSms = properties.getProperty(CLIENT_DETAIL_SMS_KEY);
        labelEmail = properties.getProperty(CLIENT_DETAIL_EMAIL_KEY);
        labelAddButton = properties.getProperty(CLIENT_DETAIL_ADD_BUTTON_KEY);
        labelEditButton = properties.getProperty(CLIENT_DETAIL_EDIT_BUTTON_KEY);
        labelCancelButton = properties.getProperty(CLIENT_DETAIL_CANCEL_BUTTON_KEY);
    }

    public void pack() {
        detailWindow.add(new JLabel(labelFirstName));
        firstName = new JTextField();
        detailWindow.add(firstName);
        detailWindow.add(new JLabel(labelLastName));
        lastName = new JTextField();
        detailWindow.add(lastName);
        detailWindow.add(new JLabel(labelSms));
        sms = new JTextField();
        detailWindow.add(sms);
        detailWindow.add(new JLabel(labelEmail));
        email = new JTextField();
        detailWindow.add(email);

        proceedButton = new JButton(labelAddButton);
        cancelButton = new JButton(labelCancelButton);
        detailWindow.add(proceedButton);
        detailWindow.add(cancelButton);
        detailWindow.pack();
    }

    public void presentEditRecord(Client client) {
        detailWindow.setVisible(false);
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        sms.setText(client.getSms());
        email.setText(client.getEmail());
        proceedButton.setText(labelEditButton);
        cancelButton.setText(labelCancelButton);
        detailWindow.setVisible(true);
    }

    public void presentAddRecord() {
        detailWindow.setVisible(false);
        firstName.setText(BLANK);
        lastName.setText(BLANK);
        sms.setText(BLANK);
        proceedButton.setText(labelAddButton);
        cancelButton.setText(labelCancelButton);
        detailWindow.setVisible(true);
    }
}
