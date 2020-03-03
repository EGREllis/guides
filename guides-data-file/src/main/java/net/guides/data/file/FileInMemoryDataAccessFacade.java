package net.guides.data.file;

import net.guides.data.InMemoryDataAccessFacade;
import net.guides.model.Client;
import net.guides.model.Event;
import net.guides.model.Payment;
import net.guides.model.PaymentType;

import java.io.BufferedReader;
import java.io.File;
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

public class FileInMemoryDataAccessFacade extends InMemoryDataAccessFacade {
    private static final Pattern CLIENT_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)~([^~]+)~([^~]+)~([^~]+)$");
    private static final Pattern EVENT_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)~([^~]+)$");
    private static final Pattern PAYMENT_TYPE_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)$");
    private static final Pattern PAYMENT_LINE_READER = Pattern.compile("^([^~]+)~([^~]+)~([^~]+)~([^~]+)~([^~]+)$");
    private Properties properties;

    public FileInMemoryDataAccessFacade(Properties properties) {
        this.properties = properties;
    }

    BufferedReader getReader(String path) throws IOException {
        return new BufferedReader(new FileReader(path));
    }

    @Override
    public void start() {
        final String clientFilePath = properties.getProperty(CLIENT_FILE_PATH_KEY);
        final String eventFilePath = properties.getProperty(EVENT_FILE_PATH_KEY);
        final String paymentTypeFilePath = properties.getProperty(PAYMENT_TYPE_FILE_PATH_KEY);
        final String paymentFilePath = properties.getProperty(PAYMENT_FILE_PATH_KEY);

        File clientFile = new File(clientFilePath);
        File eventFile = new File(eventFilePath);
        File paymentTypeFile = new File(paymentTypeFilePath);
        File paymentFile = new File(paymentFilePath);
        try {
            if (!clientFile.exists()) {
                System.out.println(String.format("Creating client file: %1$s", clientFile.getAbsolutePath()));
                clientFile.createNewFile();
            }
            if (!eventFile.exists()) {
                System.out.println(String.format("Creating client file: %1$s", eventFile.getAbsolutePath()));
                eventFile.createNewFile();
            }
            if (!paymentTypeFile.exists()) {
                System.out.println(String.format("Creating client file: %1$s", paymentTypeFile.getAbsolutePath()));
                paymentTypeFile.createNewFile();
            }
            if (!paymentFile.exists()) {
                System.out.println(String.format("Creating client file: %1$s", paymentFile.getAbsolutePath()));
                paymentFile.createNewFile();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        Map<Integer,Client> clients = new HashMap<>();
        Map<Integer,Event> events = new HashMap<>();
        Map<Integer,PaymentType> paymentTypes = new HashMap<>();

        DateFormat dateFormat = new SimpleDateFormat(properties.getProperty(DATE_FORMAT_KEY));

        try (BufferedReader reader = getReader(clientFilePath)) {
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
                this.addClient(client);
            }
            System.out.println(String.format("Clients: %1$d from %2$s", clients.size(), clientFile.getAbsoluteFile()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = getReader(eventFilePath)) {
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
                this.addEvent(event);
            }
            System.out.println(String.format("Events: %1$d from %2$s", events.size(), eventFile.getAbsolutePath()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }

        try (BufferedReader reader = getReader(paymentTypeFilePath)) {
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
                this.addPaymentType(paymentType);
            }
            System.out.println(String.format("Events: %1$d from %2$s", paymentTypes.size(), paymentTypeFile.getAbsolutePath()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        try (BufferedReader reader = getReader(paymentFilePath)) {
            String line;
            int count = 0;
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
                this.addPayment(payment);
                count++;
            }
            System.out.println(String.format("Events: %1$d from %2$s", count, paymentFile.getAbsolutePath()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    @Override
    public void stop() {
        DataAccessFileWriter fileWriter = DataAccessFileWriter.newFileWriter(this, properties);
        fileWriter.run();
    }
}
