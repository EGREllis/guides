package net.guides.data.file.writer;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.List;

public class PaymentTypeWriter implements Runnable {
    private static final String EVENT_LINE_FORMAT = "%1$d~%2$s";
    private final DataAccessFacade dataAccessFacade;
    private final String filePath;
    private final DateFormat dateFormat;

    public PaymentTypeWriter(DataAccessFacade dataAccessFacade, String filePath, DateFormat dateFormat) {
        this.dataAccessFacade = dataAccessFacade;
        this.filePath = filePath;
        this.dateFormat = dateFormat;
    }

    @Override
    public void run() {
        try (Writer writer = getWriter(filePath)) {
            List<Event> events = dataAccessFacade.getAllEvents();
            for (Event event : events) {
                writer.write(String.format(EVENT_LINE_FORMAT, event.getEventId(), event.getTitle(), dateFormat.format(event.getStartDate())));
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    Writer getWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }
}
