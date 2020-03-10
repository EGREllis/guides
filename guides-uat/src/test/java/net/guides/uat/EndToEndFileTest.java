package net.guides.uat;

import net.guides.data.DataAccessFacade;
import net.guides.data.file.FileInMemoryDataAccessFacade;
import net.guides.view.SwingView;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EndToEndFileTest {
    @Test
    public void addClient() {
        Properties properties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("uat.properties")) {
            properties.load(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        DataAccessFacade facade = new FileInMemoryDataAccessFacade(properties);
        SwingView view = new SwingView(properties, facade);
        facade.start();
        view.start();


    }
}
