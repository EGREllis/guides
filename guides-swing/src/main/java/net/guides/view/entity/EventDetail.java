package net.guides.view.entity;

import net.guides.model.Event;
import net.guides.view.Detail;

import static net.guides.view.Constants.BLANK;

import javax.swing.*;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class EventDetail implements Detail<Event> {
    private static final String EVENT_DETAIL_TITLE_KEY = "event.detail.window.title";
    private static final String EVENT_DETAIL_EVENT_TITLE_KEY = "event.detail.title";
    private static final String EVENT_DETAIL_START_DATE_KEY = "event.detail.start.date";
    private static final String EVENT_DETAIL_ADD_BUTTON_KEY = "event.detail.add.button";
    private static final String EVENT_DETAIL_EDIT_BUTTON_KEY = "event.detail.edit.button";
    private static final String EVENT_DETAIL_CANCEL_BUTTON_KEY = "event.detail.cancel.button";
    private static final String EVENT_DETAIL_DATE_FORMAT_KEY = "date.format";
    private final String labelAddButton;
    private final String labelEditButton;
    private final DateFormat dateFormat;
    private JFrame detailWindow;
    private JTextField title;
    private JTextField startDate;
    private JButton proceedButton;
    private JButton cancelButton;
    private Integer id;

    public EventDetail(Properties properties) {
        detailWindow = new JFrame(properties.getProperty(EVENT_DETAIL_TITLE_KEY));
        detailWindow.setVisible(false);
        detailWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        detailWindow.setLayout(new GridLayout(3, 2));
        labelAddButton = properties.getProperty(EVENT_DETAIL_ADD_BUTTON_KEY);
        labelEditButton = properties.getProperty(EVENT_DETAIL_EDIT_BUTTON_KEY);
        String labelCancelButton = properties.getProperty(EVENT_DETAIL_CANCEL_BUTTON_KEY);
        dateFormat = new SimpleDateFormat(properties.getProperty(EVENT_DETAIL_DATE_FORMAT_KEY));

        detailWindow.add(new JLabel(properties.getProperty(EVENT_DETAIL_EVENT_TITLE_KEY)));
        title = new JTextField();
        detailWindow.add(title);
        detailWindow.add(new JLabel(properties.getProperty(EVENT_DETAIL_START_DATE_KEY)));
        startDate = new JTextField();
        detailWindow.add(startDate);
        proceedButton = new JButton(labelAddButton);
        detailWindow.add(proceedButton);
        cancelButton = new JButton(labelCancelButton);
        detailWindow.add(cancelButton);

        detailWindow.pack();
    }

    public void presentAddRecord() {
        detailWindow.setVisible(false);
        title.setText(BLANK);
        startDate.setText(BLANK);
        proceedButton.setText(labelAddButton);
        detailWindow.setVisible(true);
        id = null;
    }

    public void presentEditRecord(Event event) {
        detailWindow.setVisible(false);
        title.setText(event.getTitle());
        startDate.setText(dateFormat.format(event.getStartDate()));
        proceedButton.setText(labelEditButton);
        detailWindow.setVisible(true);
        id = event.getEventId();
    }
}
