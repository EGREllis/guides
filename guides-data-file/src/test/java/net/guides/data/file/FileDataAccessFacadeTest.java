package net.guides.data.file;

import net.guides.data.DataAccessFacade;
import net.guides.data.DataAccessFacadeFactory;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

public class FileDataAccessFacadeTest {
    @Test
    public void test() throws ParseException {
        Properties testProperties = new Properties();
        testProperties.setProperty(FileConstants.CLIENT_FILE_PATH_KEY, "file/test-data/clients.dat");
        testProperties.setProperty(FileConstants.EVENT_FILE_PATH_KEY, "file/test-data/events.dat");
        testProperties.setProperty(FileConstants.PAYMENT_TYPE_FILE_PATH_KEY, "file/test-data/payment_types.dat");
        testProperties.setProperty(FileConstants.PAYMENT_FILE_PATH_KEY, "file/test-data/payment.dat");
        testProperties.setProperty(FileConstants.DATE_FORMAT_KEY, "yyyyMMdd");
        DataAccessFacadeFactory dataAccessFacadeFactory = new ClasspathFileDataAccessFacadeFactory(testProperties);
        DataAccessFacade facade = dataAccessFacadeFactory.newDataAccessFacade();

        List<Client> clients = facade.getAllClients();
        List<Event> events = facade.getAllEvents();
        List<PaymentType> paymentTypes = facade.getAllPaymentTypes();
        List<Payment> payments = facade.getAllPayments();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        assert clients.size() == 3;
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
}
