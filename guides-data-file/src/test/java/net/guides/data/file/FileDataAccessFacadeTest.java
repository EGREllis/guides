package net.guides.data.file;

import net.guides.data.DataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class FileDataAccessFacadeTest {
    @Before
    public void setup() {
        String[] paths = {
                "src/main/resources/file/temp-test-data/clients.dat",
                "src/main/resources/file/temp-test-data/events.dat",
                "src/main/resources/file/temp-test-data/payment_types.dat",
                "src/main/resources/file/temp-test-data/payments.dat"};
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    throw new IllegalStateException(String.format("%1$s exists but could not be deleted!", path));
                }
            }
        }
    }

    @Test
    public void test() throws ParseException {
        Properties testProperties = new Properties();
        testProperties.setProperty(FileConstants.CLIENT_FILE_PATH_KEY, "src/main/resources/file/test-data/clients.dat");
        testProperties.setProperty(FileConstants.EVENT_FILE_PATH_KEY, "src/main/resources/file/test-data/events.dat");
        testProperties.setProperty(FileConstants.PAYMENT_TYPE_FILE_PATH_KEY, "src/main/resources/file/test-data/payment_types.dat");
        testProperties.setProperty(FileConstants.PAYMENT_FILE_PATH_KEY, "src/main/resources/file/test-data/payment.dat");
        testProperties.setProperty(FileConstants.DATE_FORMAT_KEY, "yyyyMMdd");
        DataAccessFacade facade = new FileInMemoryDataAccessFacade(testProperties);
        facade.start();

        List<Client> clients = facade.getAllClients();
        List<Event> events = facade.getAllEvents();
        List<PaymentType> paymentTypes = facade.getAllPaymentTypes();
        List<Payment> payments = facade.getAllPayments();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        assert clients.size() == 3 : String.format("Actual: %1$d Expected: %2$d", clients.size(), 3);
        Client client = clients.get(0);
        assert client.getClientId() == 1;
        assert client.getFirstName().equals("Gary");
        assert client.getLastName().equals("Blower");
        assert client.getSms().equals("07853286000");
        assert client.getEmail().equals("a@b.com");

        assert events.size() == 2;
        Event event = events.get(0);
        assert event.getEventId() == 1;
        assert event.getTitle().equals("Test event");
        assert event.getStartDate().equals(dateFormat.parse("20191230"));

        assert paymentTypes.size() == 2;
        PaymentType paymentType = paymentTypes.get(0);
        assert paymentType.getId() == 1;
        assert paymentType.getDescription().equals("Cash");

        assert payments.size() == 1;
        Payment payment = payments.get(0);
        assert payment.getPaymentId() == 1;
        assert payment.getClient().getClientId() == 2;
        assert payment.getEventId().getEventId() == 3;
        assert payment.getPaymentTypeId().getId() == 4;
    }

    @Test
    public void can_read_filesThatHaveBeenSaved() {
        Properties testProperties = new Properties();
        testProperties.setProperty(FileConstants.CLIENT_FILE_PATH_KEY, "src/main/resources/file/temp-test-data/clients.dat");
        testProperties.setProperty(FileConstants.EVENT_FILE_PATH_KEY, "src/main/resources/file/temp-test-data/events.dat");
        testProperties.setProperty(FileConstants.PAYMENT_TYPE_FILE_PATH_KEY, "src/main/resources/file/temp-test-data/payment_types.dat");
        testProperties.setProperty(FileConstants.PAYMENT_FILE_PATH_KEY, "src/main/resources/file/temp-test-data/payment.dat");
        testProperties.setProperty(FileConstants.DATE_FORMAT_KEY, "yyyyMMdd");
        DataAccessFacade facade = new FileInMemoryDataAccessFacade(testProperties);
        facade.start();
        facade.addClient(new Client(5, "Edward", "Ellis", "07853123456", "edward.ellis@rbs.com"));
        assert facade.getAllClients().size() == 1;
        facade.stop();
        facade.start();
        assert facade.getAllClients().size() == 1;
    }
}
