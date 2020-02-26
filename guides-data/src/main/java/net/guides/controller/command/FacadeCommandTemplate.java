package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.controller.Command;
import net.guides.controller.Listener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class FacadeCommandTemplate<T> implements Command<T> {
    private static final String ERROR_ADDING_CLIENT_MESSAGE_KEY = "error.message.%1$s";
    private static final String ERROR_ADDING_CLIENT_TITLE_KEY = "error.title.%1$s";
    private final String prefix;
    protected final DataAccessFacade facade;
    private final Properties properties;
    private final List<Listener> listeners;

    public FacadeCommandTemplate(DataAccessFacade facade, Properties properties, String prefix) {
        this.prefix = prefix;
        this.facade = facade;
        this.properties = properties;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void execute(T item) {
        if (doTheJob(item)) {
            for (Listener listener : listeners) {
                listener.alert();
            }
        } else {
            JFrame dialog = new JFrame();
            JOptionPane.showMessageDialog(dialog,
                    properties.getProperty(String.format(ERROR_ADDING_CLIENT_MESSAGE_KEY, prefix)),
                    properties.getProperty(String.format(ERROR_ADDING_CLIENT_TITLE_KEY, prefix)),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    protected abstract boolean doTheJob(T item);
}
