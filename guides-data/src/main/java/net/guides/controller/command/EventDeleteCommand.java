package net.guides.controller.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;
import net.guides.controller.Command;

import java.util.Properties;

public class EventDeleteCommand extends FacadeCommandTemplate<Event> implements Command<Event> {
    public EventDeleteCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "event.delete");
    }

    @Override
    protected boolean doTheJob(Event event) {
        return facade.removeEvent(event.getEventId());
    }
}
