package net.guides.data.file;

import net.guides.data.DataAccessFacade;
import net.guides.data.DataAccessFacadeFactory;
import net.guides.data.InMemoryDataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.guides.data.file.FileConstants.*;

public class FileDataAccessFacadeFactory implements DataAccessFacadeFactory {
    private static final Pattern CLIENT_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)~([^~]+)~([^~]+)~([^~]+)$");
    private static final Pattern EVENT_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)~([^~]+)$");
    private static final Pattern PAYMENT_TYPE_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)$");
    private static final Pattern PAYMENT_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)~([^~]+)~([^~]+)~([^~]+)$");

    public FileDataAccessFacadeFactory(Properties properties) {
        this.properties = properties;
    }

    BufferedReader getReader(String path) throws IOException {
        return new BufferedReader(new FileReader(path));
    }

    private final Properties properties;

    @Override
    public DataAccessFacade newDataAccessFacade() {
        Map<Integer,Client> clients = new HashMap<>();
        Map<Integer,Event> events = new HashMap<>();
        Map<Integer,PaymentType> paymentTypes = new HashMap<>();
        Map<Integer,Payment> payments = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat(properties.getProperty(DATE_FORMAT_KEY));

        try (BufferedReader reader = getReader(properties.getProperty(CLIENT_FILE_PATH_KEY))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                Matcher matcher = CLIENT_LINE_READER.matcher(line);
                if (!matcher.matches()) {
                    throw new RuntimeException("Client line did not match line reader expectations");
                }
                Integer clientId = Integer.valueOf(matcher.group(1));
                String firstName = matcher.group(2);
                String lastName = matcher.group(3);
                String sms = matcher.group(4);
                String email = matcher.group(5);
                Client client = new Client(clientId, firstName, lastName, sms, email);
                clients.put(client.getClientId(), client);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = getReader(properties.getProperty(EVENT_FILE_PATH_KEY))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                Matcher matcher = EVENT_LINE_READER.matcher(line);
                if (!matcher.matches()) {
                    throw new RuntimeException("Client line did not match line reader expectations");
                }
                Integer eventId = Integer.parseInt(matcher.group(1));
                String title = matcher.group(2);
                Date eventDate = dateFormat.parse(matcher.group(3));
                Event event = new Event(eventId, title, eventDate);
                events.put(event.getEventId(), event);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }

        try (BufferedReader reader = getReader(properties.getProperty(PAYMENT_TYPE_FILE_PATH_KEY))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                Matcher matcher = PAYMENT_TYPE_LINE_READER.matcher(line);
                if (!matcher.matches()) {
                    throw new RuntimeException("Client line did not match line reader expectations");
                }
                Integer paymentTypeId = Integer.parseInt(matcher.group(1));
                String description = matcher.group(2);
                PaymentType paymentType = new PaymentType(paymentTypeId, description);
                paymentTypes.put(paymentType.getId(), paymentType);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        try (BufferedReader reader = getReader(properties.getProperty(PAYMENT_FILE_PATH_KEY))) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                Matcher matcher = PAYMENT_LINE_READER.matcher(line);
                if (!matcher.matches()) {
                    throw new RuntimeException("Client line did not match line reader expectations");
                }
                Integer paymentId = Integer.parseInt(matcher.group(1));
                int clientIndex = Integer.parseInt(matcher.group(2));
                Client client = clients.get(clientIndex);
                int eventIndex = Integer.parseInt(matcher.group(3));
                Event event = events.get(eventIndex);
                int paymentTypeIndex = Integer.parseInt(matcher.group(4));
                PaymentType paymentType = paymentTypes.get(paymentTypeIndex);
                Date paymentDate = dateFormat.parse(matcher.group(5));
                Payment payment = new Payment(paymentId, client, event, paymentType, paymentDate);
                payments.put(paymentId, payment);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }

        return InMemoryDataAccessFacade.stocked(clients, events, paymentTypes, payments);
    }
}
