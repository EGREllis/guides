package net.guides.view.command;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;
import net.guides.view.Command;

import java.util.Properties;

public class EventEditCommand extends FacadeCommandTemplate<Event> implements Command<Event> {
    public EventEditCommand(DataAccessFacade facade, Properties properties) {
        super(facade, properties, "event.edit");
    }

    @Override
    protected boolean doTheJob(Event event) {
        return facade.updateEvent(event);
    }
}
