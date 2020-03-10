package net.guides.view.detail;

import static net.guides.view.Constants.BLANK;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.controller.Command;
import net.guides.view.Detail;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Properties;

public class ClientDetail implements Detail<Client> {
    public static final String CLIENT_DETAIL_KEY = "client.detail";
    public static final String CLIENT_DETAIL_TITLE_KEY = "client.detail.window.title";
    public static final String CLIENT_DETAIL_FIRST_NAME_KEY = "client.detail.first.name";
    public static final String CLIENT_DETAIL_LAST_NAME_KEY = "client.detail.last.name";
    public static final String CLIENT_DETAIL_SMS_KEY = "client.detail.sms";
    public static final String CLIENT_DETAIL_EMAIL_KEY = "client.detail.email";
    public static final String CLIENT_DETAIL_ADD_BUTTON_KEY = "client.detail.add.button";
    public static final String CLIENT_DETAIL_EDIT_BUTTON_KEY = "client.detail.edit.button";
    public static final String CLIENT_DETAIL_CANCEL_BUTTON_KEY = "client.detail.cancel.button";
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
    private Integer id;
    private ActionListener addButtonListener;
    private ActionListener editButtonListener;

    public ClientDetail(Properties properties, final Command<Client> addCommand, final Command<Client> editCommand, Map<String,Component> componentMap) {
        detailWindow = new JFrame(properties.getProperty(CLIENT_DETAIL_TITLE_KEY));
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setVisible(false);
        detailWindow.setLayout(new GridLayout(5, 2));
        labelAddButton = properties.getProperty(CLIENT_DETAIL_ADD_BUTTON_KEY);
        labelEditButton = properties.getProperty(CLIENT_DETAIL_EDIT_BUTTON_KEY);
        labelCancelButton = properties.getProperty(CLIENT_DETAIL_CANCEL_BUTTON_KEY);

        detailWindow.add(new JLabel(properties.getProperty(CLIENT_DETAIL_FIRST_NAME_KEY)));
        firstName = new JTextField();
        detailWindow.add(firstName);
        detailWindow.add(new JLabel(properties.getProperty(CLIENT_DETAIL_LAST_NAME_KEY)));
        lastName = new JTextField();
        detailWindow.add(lastName);
        detailWindow.add(new JLabel(properties.getProperty(CLIENT_DETAIL_SMS_KEY)));
        sms = new JTextField();
        detailWindow.add(sms);
        detailWindow.add(new JLabel(properties.getProperty(CLIENT_DETAIL_EMAIL_KEY)));
        email = new JTextField();
        detailWindow.add(email);

        proceedButton = new JButton(labelAddButton);
        cancelButton = new JButton(labelCancelButton);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detailWindow.setVisible(false);
                firstName.setText(BLANK);
                lastName.setText(BLANK);
                sms.setText(BLANK);
                email.setText(BLANK);
                //TODO: Dispatch to update
            }
        });
        addButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = getRecord();
                addCommand.execute(client);
                detailWindow.setVisible(false);
            }
        };
        editButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = getRecord();
                editCommand.execute(client);
                detailWindow.setVisible(false);
            }
        };
        detailWindow.add(proceedButton);
        detailWindow.add(cancelButton);
        detailWindow.pack();

        componentMap.put(CLIENT_DETAIL_FIRST_NAME_KEY, firstName);
        componentMap.put(CLIENT_DETAIL_LAST_NAME_KEY, lastName);
        componentMap.put(CLIENT_DETAIL_SMS_KEY, sms);
        componentMap.put(CLIENT_DETAIL_EMAIL_KEY, email);
        componentMap.put(CLIENT_DETAIL_ADD_BUTTON_KEY, proceedButton);
        componentMap.put(CLIENT_DETAIL_EDIT_BUTTON_KEY, proceedButton); // Same button for both purposes
        componentMap.put(CLIENT_DETAIL_CANCEL_BUTTON_KEY, cancelButton);
        componentMap.put(CLIENT_DETAIL_KEY, detailWindow);
    }

    private Client getRecord() {
        Integer newId = id;
        String newFirstName = firstName.getText();
        String newLastName = lastName.getText();
        String newSms = sms.getText();
        String newEmail = email.getText();
        return new Client(newId, newFirstName, newLastName, newSms, newEmail);
    }

    public void presentEditRecord(Client client) {
        detailWindow.setVisible(false);
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        sms.setText(client.getSms());
        email.setText(client.getEmail());
        proceedButton.setText(labelEditButton);
        proceedButton.addActionListener(editButtonListener);
        proceedButton.removeActionListener(addButtonListener);
        cancelButton.setText(labelCancelButton);
        detailWindow.setVisible(true);
        id = client.getClientId();
    }

    public void presentAddRecord() {
        detailWindow.setVisible(false);
        firstName.setText(BLANK);
        lastName.setText(BLANK);
        sms.setText(BLANK);
        email.setText(BLANK);
        proceedButton.setText(labelAddButton);
        proceedButton.addActionListener(addButtonListener);
        proceedButton.removeActionListener(editButtonListener);
        cancelButton.setText(labelCancelButton);
        detailWindow.setVisible(true);
        id = null;
    }
}
