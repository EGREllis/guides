package net.guides.data.file;

import net.guides.data.DataAccessFacade;
import net.guides.data.file.writer.ClientWriter;
import net.guides.data.file.writer.EventWriter;
import net.guides.data.file.writer.PaymentTypeWriter;
import net.guides.data.file.writer.PaymentWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DataAccessFileWriter implements Runnable {
    private static final int THREAD_COUNT = 4;
    private final DataAccessFacade dataAccessFacade;
    private final String clientFilePath;
    private final String eventFilePath;
    private final String paymentFilePath;
    private final String paymentTypeFilePath;
    private final DateFormat dateFormat;

    public DataAccessFileWriter(DataAccessFacade dataAccessFacade, String clientFilePath, String eventFilePath, String paymentFilePath, String paymentTypeFilePath, DateFormat dateFormat) {
        this.dataAccessFacade = dataAccessFacade;
        this.clientFilePath = clientFilePath;
        this.eventFilePath = eventFilePath;
        this.paymentFilePath = paymentFilePath;
        this.paymentTypeFilePath = paymentTypeFilePath;
        this.dateFormat = dateFormat;
    }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Future<? extends Object>> futures = new ArrayList<>(THREAD_COUNT);
        futures.add(executorService.submit(new ClientWriter(dataAccessFacade, clientFilePath)));
        futures.add(executorService.submit(new EventWriter(dataAccessFacade, eventFilePath, dateFormat)));
        futures.add(executorService.submit(new PaymentTypeWriter(dataAccessFacade, paymentTypeFilePath)));
        futures.add(executorService.submit(new PaymentWriter(dataAccessFacade, paymentFilePath, dateFormat)));
        for (Future<? extends Object> future : futures) {
            future.isDone();
        }
    }

    public static DataAccessFileWriter newFileWriter(DataAccessFacade dataAccessFacade, Properties properties) {
        return new DataAccessFileWriter(
                dataAccessFacade,
                properties.getProperty(FileConstants.CLIENT_FILE_PATH_KEY),
                properties.getProperty(FileConstants.EVENT_FILE_PATH_KEY),
                properties.getProperty(FileConstants.PAYMENT_FILE_PATH_KEY),
                properties.getProperty(FileConstants.PAYMENT_TYPE_FILE_PATH_KEY),
                new SimpleDateFormat(properties.getProperty(FileConstants.DATE_FORMAT_KEY)));
    }
}
