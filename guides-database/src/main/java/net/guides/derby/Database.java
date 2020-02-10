package net.guides.derby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
    private static final Pattern DAT_FILE_PATTERN = Pattern.compile("([\\S]+)\\s+([\\S]+.sql)\\s*");
    private static final String EXCEPTION_UNEXPECTED_TYPE = "File: %1$s \tDid not expect 'type' %2$s";
    private static final String CREATE_TABLE_LOCATION_FORMAT = "ddl/%1$s";
    private static final String DAT_FILE_ERROR_FORMAT = "Line in %1$s did not match expectations : %2$s";
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
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CREATE_TABLE_LOCATION)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            List<SqlCommand> sqlCommands = new ArrayList<>();
            while ( ((line = reader.readLine()) != null)) {
                Matcher matcher = DAT_FILE_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String type = matcher.group(1);
                    String file = matcher.group(2);
                    System.out.println(String.format("%1$s\t%2$s", type, file));
                    String location = String.format(CREATE_TABLE_LOCATION_FORMAT, file);
                    SqlCommand command = createSqlCommand(type, location);
                    sqlCommands.add(command);
                } else {
                    System.err.println(String.format(DAT_FILE_ERROR_FORMAT, CREATE_TABLE_LOCATION, line));
                }
            }
            boolean clean = true;
            for (int i = sqlCommands.size()-1; i > 0 && clean; i--) {
                SqlCommand command = sqlCommands.get(i);
                try {
                    clean = command.prepare();
                } catch (SQLException sqle) {
                    throw new RuntimeException(sqle);
                }
            }
            for (int i = 0; i < sqlCommands.size() && clean; i++) {
                SqlCommand command = sqlCommands.get(i);
                try {
                    clean = command.execute();
                } catch (SQLException sqle) {
                    throw new RuntimeException(sqle);
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    SqlCommand createSqlCommand(String type, String location) {
        SqlCommand command = null;
        switch(type) {
            case "create_table":
                command = new CreateTableSqlCommand(this, location, DROP_TABLE_IF_FOUND);
                break;
            default:
                throw new IllegalStateException(String.format(EXCEPTION_UNEXPECTED_TYPE, location, type));
        }
        return command;
    }
}
