package net.guides.data.file.writer;

import net.guides.data.DataAccessFacade;
import net.guides.model.Event;
import net.guides.model.PaymentType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.List;

public class PaymentTypeWriter implements Runnable {
    private static final String EVENT_LINE_FORMAT = "%1$d~%2$s";
    private final DataAccessFacade dataAccessFacade;
    private final String filePath;

    public PaymentTypeWriter(DataAccessFacade dataAccessFacade, String filePath) {
        this.dataAccessFacade = dataAccessFacade;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try (Writer writer = getWriter(filePath)) {
            List<PaymentType> paymentTypes = dataAccessFacade.getAllPaymentTypes();
            for (PaymentType paymentType : paymentTypes) {
                writer.write(String.format(EVENT_LINE_FORMAT, paymentType.getId(), paymentType.getDescription()));
            }
            System.out.println(String.format("Saved %1$d records to payment types file %2$s", paymentTypes.size(), new File(filePath).getAbsolutePath()));
            System.out.flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    Writer getWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }
}
