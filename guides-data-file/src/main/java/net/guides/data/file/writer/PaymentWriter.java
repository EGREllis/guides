package net.guides.data.file.writer;

import net.guides.data.DataAccessFacade;
import net.guides.model.Payment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.List;

public class PaymentWriter implements Runnable {
    private static final String PAYMENT_LINE_FORMAT = "%1$d~%2$d~%3$d~%4$d~%5$s";
    private final DateFormat dateFormat;
    private final String filePath;
    private final DataAccessFacade dataAccessFacade;

    public PaymentWriter(DataAccessFacade dataAccessFacade, String filePath, DateFormat dateFormat) {
        this.dataAccessFacade = dataAccessFacade;
        this.filePath = filePath;
        this.dateFormat = dateFormat;
    }

    @Override
    public void run() {
        try (Writer writer = getWriter(filePath)) {
            List<Payment> payments = dataAccessFacade.getAllPayments();
            for (Payment payment : payments) {
                writer.write(String.format(PAYMENT_LINE_FORMAT,
                        payment.getPaymentId(),
                        payment.getClient().getClientId(),
                        payment.getEventId().getEventId(),
                        payment.getPaymentTypeId().getId(),
                        dateFormat.format(payment.getPaymentDate())));
            }
            System.out.println(String.format("Saved %1$d records to payments file %2$s", payments.size(), new File(filePath).getAbsolutePath()));
            System.out.flush();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    Writer getWriter(String filePath) throws IOException {
        return new FileWriter(filePath);
    }
}
