package net.guides.uat;

import net.guides.data.DataAccessFacade;
import net.guides.data.file.FileConstants;
import net.guides.data.file.FileInMemoryDataAccessFacade;
import net.guides.view.SwingView;
import net.guides.view.tab.TabImpl;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        facade = new FileInMemoryDataAccessFacade(properties);
        view = new SwingView(properties, facade);
        facade.start();
        view.start();
    }

    @Test
    public void addClient() {
        Map<String,Component> swingComponents = view.getComponents();
        assertTableRows(String.format(TabImpl.TAB_TABLE_TEXT_KEY, SwingView.CLIENT_TAB_PREFIX), 0);
    }

    public void assertTableRows(String componentKey, int nRows) {
        JTable table = (JTable)view.getComponents().get(componentKey);
        assert table.getRowCount() == nRows;
    }
}
