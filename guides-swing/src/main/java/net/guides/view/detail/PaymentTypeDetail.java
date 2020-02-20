package net.guides.view.detail;

import net.guides.model.PaymentType;
import net.guides.view.Constants;
import net.guides.view.Detail;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class PaymentTypeDetail implements Detail<PaymentType> {
    private static final String PAYMENT_TYPE_DETAIL_WINDOW_KEY = "payment.type.detail.window.title";
    private static final String PAYMENT_TYPE_DESCRIPTION_KEY = "payment.type.detail.description";
    private static final String PAYMENT_TYPE_ADD_BUTTON_KEY = "payment.type.detail.add.button";
    private static final String PAYMENT_TYPE_EDIT_BUTTON_KEY = "payment.type.detail.edit.button";
    private static final String PAYMENT_TYPE_CANCEL_BUTTON_KEY = "payment.type.detail.cancel.button";
    private final String addButtonLabel;
    private final String editButtonLabel;
    private final String cancelButtonLabel;
    private JFrame detailWindow;
    private JTextField description;
    private JButton proceedButton;
    private JButton cancelButton;
    private Integer id;

    public PaymentTypeDetail(Properties properties) {
        detailWindow = new JFrame(properties.getProperty(PAYMENT_TYPE_DETAIL_WINDOW_KEY));
        detailWindow.setVisible(false);
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setLayout(new GridLayout(2, 1));
        addButtonLabel = properties.getProperty(PAYMENT_TYPE_ADD_BUTTON_KEY);
        editButtonLabel = properties.getProperty(PAYMENT_TYPE_EDIT_BUTTON_KEY);
        cancelButtonLabel = properties.getProperty(PAYMENT_TYPE_CANCEL_BUTTON_KEY);

        detailWindow.add(new JLabel(properties.getProperty(PAYMENT_TYPE_DESCRIPTION_KEY)));
        description = new JTextField();
        detailWindow.add(description);
        proceedButton = new JButton(addButtonLabel);
        detailWindow.add(proceedButton);
        cancelButton = new JButton(cancelButtonLabel);
        detailWindow.add(cancelButton);
        detailWindow.pack();
    }

    @Override
    public void presentAddRecord() {
        detailWindow.setVisible(false);
        description.setText(Constants.BLANK);
        proceedButton.setText(addButtonLabel);
        detailWindow.setVisible(true);
        id = null;
    }

    @Override
    public void presentEditRecord(PaymentType record) {
        detailWindow.setVisible(false);
        description.setText(record.getDescription());
        proceedButton.setText(editButtonLabel);
        detailWindow.setVisible(true);
        id = record.getId();
    }
}
