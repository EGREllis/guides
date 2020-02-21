package net.guides.main;

import net.guides.data.DataAccessFacade;
import net.guides.data.InMemoryDataAccessFacade;
import net.guides.data.LoggingDataAccessFacadeDecorator;
import net.guides.data.StubbedDataAccessFacade;
import net.guides.view.SwingView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SwingMain {
    private static final String SWING_PROPERTIES_PATH = "labels/swing-labels.properties";

    public static void main(String args[]) {
        Properties swingProperties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(SWING_PROPERTIES_PATH)) {
            swingProperties.load(inputStream);
        } catch (IOException ioe) {
            throw new IllegalStateException();
        }
        DataAccessFacade facade = new LoggingDataAccessFacadeDecorator(InMemoryDataAccessFacade.stockedWithDummyData());
        SwingView view = new SwingView(swingProperties, facade);
        view.start();
    }
}
