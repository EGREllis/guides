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
import java.util.Properties;

public class ClientDetail implements Detail<Client> {
    private static final String CLIENT_DETAIL_TITLE_KEY = "client.detail.window.title";
    private static final String CLIENT_DETAIL_FIRST_NAME_KEY = "client.detail.first.name";
    private static final String CLIENT_DETAIL_LAST_NAME_KEY = "client.detail.last.name";
    private static final String CLIENT_DETAIL_SMS_KEY = "client.detail.sms";
    private static final String CLIENT_DETAIL_EMAIL_KEY = "client.detail.email";
    private static final String CLIENT_DETAIL_ADD_BUTTON_KEY = "client.detail.add.button";
    private static final String CLIENT_DETAIL_EDIT_BUTTON_KEY = "client.detail.edit.button";
    private static final String CLIENT_DETAIL_CANCEL_BUTTON_KEY = "client.detail.cancel.button";
    private final DataAccessFacade facade;
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
    private Command<Client> addCommand;
    private Command<Client> editCommand;
    private Command<Client> deleteCommand;

    public ClientDetail(Properties properties, final DataAccessFacade facade, final Command<Client> addCommand, final Command<Client> editCommand, Command<Client> deleteCommand) {
        this.facade = facade;
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

        this.addCommand = addCommand;
        this.editCommand = editCommand;
        this.deleteCommand = deleteCommand;

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
            }
        };
        editButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client client = getRecord();
                editCommand.execute(client);
            }
        };
        detailWindow.add(proceedButton);
        detailWindow.add(cancelButton);
        detailWindow.pack();
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
        proceedButton.setText(labelAddButton);
        proceedButton.addActionListener(addButtonListener);
        proceedButton.removeActionListener(editButtonListener);
        cancelButton.setText(labelCancelButton);
        detailWindow.setVisible(true);
        id = null;
    }
}
