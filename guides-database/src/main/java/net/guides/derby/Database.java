package net.guides.derby;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Database {
    private static final String DATABASE_PROPERTIES_LOCATION = "properties/db_connection.properties";
    private static final String CREATE_TABLE_LOCATION = "ddl/create_table.dat";
    private static final String SQL_DRIVER_KEY = "DB_DRIVER_CLASS";
    private static final String DB_URL_KEY = "JDBC_URL";
    private static final String DB_USERNAME_KEY = "DB_USERNAME";
    private static final String DB_PASSWORD_KEY = "DB_PASSWORD";
    private static final boolean DROP_TABLE_IF_FOUND = true;

    private Properties dbProperties;

    public void start() throws Exception {
        dbProperties = getDatabaseProperties();
        registerDriver(dbProperties);
        System.out.println(dbProperties);
        createTables();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProperties.getProperty(DB_URL_KEY));
    }

    private Properties getDatabaseProperties() {
        Properties dbProperties = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(DATABASE_PROPERTIES_LOCATION)) {
            dbProperties.load(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return dbProperties;
    }

    private void registerDriver(Properties properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        String className = properties.getProperty(SQL_DRIVER_KEY);
        Driver driver = (Driver)Class.forName(className).newInstance();
        DriverManager.registerDriver(driver);
    }

    private void createTables() {
        CreateTableBatchCommand batch = new CreateTableBatchCommand(this, CREATE_TABLE_LOCATION, DROP_TABLE_IF_FOUND);
        try {
            boolean clean = batch.prepare();
            clean = batch.execute();
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }
}
