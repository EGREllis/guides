package net.guides.model;

import java.util.Date;

public class Event {
    private int eventId;
    private String title;
    private Date startDate;

    public Event(int eventId, String title, Date startDate) {
        this.eventId = eventId;
        this.title = title;
        this.startDate = startDate;
    }

    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Event replaceId(int eventId) {
        return new Event(eventId, title, startDate);
    }

    @Override
    public String toString() {
        return String.format("EventId:%1$d %2$s at %3$s", eventId, title, startDate);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Event) {
            Event other = (Event)obj;
            result = title.equals(other.title) && startDate.equals(other.startDate);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return eventId;
    }
}
