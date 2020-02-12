package net.guides.storage;

import net.guides.model.Event;

import java.util.List;

public interface EventRepository {
    List<Event> getEvents();
    boolean addEvent(Event event);
}
