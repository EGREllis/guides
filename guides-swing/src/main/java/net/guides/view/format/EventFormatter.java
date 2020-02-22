package net.guides.view.format;

import net.guides.model.Event;
import net.guides.view.Formatter;

import java.util.Properties;

public class EventFormatter implements Formatter<Event> {
    private static final String EVENT_FORMATTER_KEY = "event.combo.box.format";
    private final String format;

    public EventFormatter(Properties properties) {
        this.format = properties.getProperty(EVENT_FORMATTER_KEY);
    }

    @Override
    public String format(Event event) {
        return String.format(format, event.getTitle(), event.getStartDate());
    }
}
