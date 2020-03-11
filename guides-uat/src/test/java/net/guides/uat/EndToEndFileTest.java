package net.guides.uat;

import net.guides.data.DataAccessFacade;
import net.guides.data.file.FileConstants;
import net.guides.data.file.FileInMemoryDataAccessFacade;
import net.guides.view.SwingView;
import net.guides.view.detail.ClientDetail;
import net.guides.view.tab.TabImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EndToEndFileTest {
    private Properties properties = new Properties();
    private DataAccessFacade facade;
    private SwingView view;

    public EndToEndFileTest() {
        try (InputStream inputStream = new FileInputStream("../guides-compose/src/main/resources/composed.properties")) {
            properties.load(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @Before
    public void setup() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
            File clientFile = new File(properties.getProperty(FileConstants.CLIENT_FILE_PATH_KEY));
            if (clientFile.exists()) {
                if (!clientFile.delete()) {
                    throw new IllegalStateException("Client file exists, but could not be deleted!");
                }
            }
            File eventFile = new File(properties.getProperty(FileConstants.EVENT_FILE_PATH_KEY));
            if (eventFile.exists()) {
                if (eventFile.delete()) {
                    throw new IllegalStateException("Event file exists, but could not be deleted!");
                }
            }
            File paymentTypeFile = new File(properties.getProperty(FileConstants.PAYMENT_TYPE_FILE_PATH_KEY));
            if (paymentTypeFile.exists()) {
                if (paymentTypeFile.delete()) {
                    throw new IllegalStateException("Payment type exists, but could not be deleted!");
                }
            }
            File paymentFile = new File(properties.getProperty(FileConstants.PAYMENT_FILE_PATH_KEY));
            if (paymentFile.exists()) {
                if (paymentFile.delete()) {
                    throw new IllegalStateException("Payment exists, but could not be deleted!");
                }
            }
        facade = new FileInMemoryDataAccessFacade(properties);
        view = SwingView.newTestSwingView(properties, facade);
        facade.start();
        view.start();
    }

    @After
    public void teardown() {
        facade.stop();
        view.stop();
    }

    @Test
    public void when_started_then_noClientsPresent() {
        assertTableRows(String.format(TabImpl.TAB_TABLE_TEXT_KEY, SwingView.CLIENT_TAB_PREFIX), 0);
    }

    @Test
    public void after_clientAdded_then_oneClientPresent() {
        String addClientKey = String.format(TabImpl.TAB_ADD_BUTTON_TEXT_KEY, SwingView.CLIENT_TAB_PREFIX);
        clickButton(addClientKey, "Add client");
        String firstNameKey = String.format(ClientDetail.CLIENT_DETAIL_FIRST_NAME_KEY, SwingView.CLIENT_TAB_PREFIX);
        String lastNameKey = String.format(ClientDetail.CLIENT_DETAIL_LAST_NAME_KEY, SwingView.CLIENT_TAB_PREFIX);
        String smsKey = String.format(ClientDetail.CLIENT_DETAIL_SMS_KEY, SwingView.CLIENT_TAB_PREFIX);
        String email = String.format(ClientDetail.CLIENT_DETAIL_EMAIL_KEY, SwingView.CLIENT_TAB_PREFIX);
        String addClientDetailButton = String.format(ClientDetail.CLIENT_DETAIL_ADD_BUTTON_KEY, SwingView.CLIENT_TAB_PREFIX);
        setText(firstNameKey, "Christine");
        setText(lastNameKey, "Bashford");
        setText(smsKey, "07853123456");
        setText(email, "CBashford@some.place.com");
        clickButton(addClientDetailButton, "Add client...");
        String clientTableKey = String.format(TabImpl.TAB_TABLE_TEXT_KEY, SwingView.CLIENT_TAB_PREFIX);
        assertTableRows(clientTableKey, 1);
        assertTableValue(clientTableKey, 0, 0, "Christine");
        assertTableValue(clientTableKey, 0, 1, "Bashford");
        assertTableValue(clientTableKey, 0, 2, "07853123456");
        assertTableValue(clientTableKey, 0, 3, "CBashford@some.place.com");
    }

    public void setText(String key, String text) {
        JTextField textField = (JTextField)view.getComponents().get(key);
        assert textField.isVisible();
        textField.setText(text);
    }

    public void clickButton(String key, String buttonText) {
        JButton button = (JButton)view.getComponents().get(key);
        button.doClick();
        assert button.isVisible();
        assert button.getText().equals(buttonText);
    }

    public void assertTableRows(String componentKey, int nRows) {
        JTable table = (JTable)view.getComponents().get(componentKey);
        assert table.getRowCount() == nRows;
    }

    public void assertTableValue(String key, int row, int col, Object expected) {
        JTable table = (JTable)view.getComponents().get(key);
        Object actual = table.getValueAt(row, col);
        assert expected.equals(actual) : String.format("Actual: %1$s Expected: %2$s", actual, expected);
    }
}
