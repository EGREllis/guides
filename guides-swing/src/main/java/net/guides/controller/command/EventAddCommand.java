package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;
import net.guides.controller.Command;

import java.util.Properties;

public class EventAddCommand extends FacadeCommandTemplate<Event> implements Command<Event> {
    public EventAddCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "event.add");
    }

    @Override
    protected boolean doTheJob(Event event) {
        return facade.addEvent(event);
    }
}
