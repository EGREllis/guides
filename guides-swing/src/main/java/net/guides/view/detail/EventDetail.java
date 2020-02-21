package net.guides.view.detail;

import net.guides.model.Event;
import net.guides.controller.Command;
import net.guides.view.Detail;

import static net.guides.view.Constants.BLANK;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final ActionListener addButtonListener;
    private final ActionListener editButtonListener;
    private final Command<Event> addEventCommand;
    private final Command<Event> editEventCommand;
    private final Command<Event> deleteEventCommand;

    public EventDetail(Properties properties, final Command<Event> addCommand, final Command<Event> editCommand, Command<Event> deleteCommand) {
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

        this.addEventCommand = addCommand;
        this.editEventCommand = editCommand;
        this.deleteEventCommand = deleteCommand;

        addButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event thisEvent = getRecord();
                addCommand.execute(thisEvent);
            }
        };
        editButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Event thisEvent = getRecord();
                editCommand.execute(thisEvent);
            }
        };

        detailWindow.pack();
    }

    private Event getRecord() {
        String titleText = title.getText();
        Date eventStartDate;
        try {
            eventStartDate = dateFormat.parse(startDate.getText());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return new Event(id, titleText, eventStartDate);
    }

    public void presentAddRecord() {
        detailWindow.setVisible(false);
        title.setText(BLANK);
        startDate.setText(BLANK);
        proceedButton.setText(labelAddButton);
        proceedButton.addActionListener(addButtonListener);
        proceedButton.removeActionListener(editButtonListener);
        detailWindow.setVisible(true);
        id = null;
    }

    public void presentEditRecord(Event event) {
        detailWindow.setVisible(false);
        title.setText(event.getTitle());
        startDate.setText(dateFormat.format(event.getStartDate()));
        proceedButton.setText(labelEditButton);
        proceedButton.addActionListener(editButtonListener);
        proceedButton.removeActionListener(addButtonListener);
        detailWindow.setVisible(true);
        id = event.getEventId();
    }
}
