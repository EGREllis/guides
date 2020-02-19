package net.guides.view.loader;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;
import net.guides.view.Loader;

import java.util.List;

public class EventLoader implements Loader<Event> {
    private final DataAccessFacade facade;

    public EventLoader(DataAccessFacade facade) {
        this.facade = facade;
    }

    @Override
    public List<Event> load() {
        return facade.getAllEvents();
    }
}
