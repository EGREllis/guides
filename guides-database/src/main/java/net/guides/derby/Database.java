package net.guides.derby;

import net.guides.derby.sql.CreateTableBatchCommand;
import net.guides.derby.sql.InsertDataSqlBatchCommand;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Database {
    public static final String DATABASE_DATE_FORMAT = "sql.date.format";
    private static final String DATABASE_PROPERTIES_LOCATION = "properties/db_connection.properties";
    private static final String CREATE_TABLE_LOCATION = "ddl/create_table.dat";
    private static final String INSERT_TABLE_LOCATION = "dml/insert_data.dat";
    private static final String INSERT_TABLE_TEST_DATA = "dml/insert_test_data.dat";

    private static final String EXCEPTION_EXPECTED_RESULT_SET_NOT_UPDATE_COUNT = "Expected to execute a query returning a result set, but received an update count instead: %1$s";
    private static final String EXCEPTION_EXPECTED_UPDATE_COUNT_NOT_RESULT_SET = "Expected to execute a query returning an update count, but received a result set instead: %1$s";
    private static final String EXCEPTION_EXPECTED_NON_ZERO_RESULT_SET = "Expected at least one result set: %1$s";
    private static final String EXCEPTION_EXPECTED_EXACTLY_ONE_RESULT_SET = "Expected exactly one result set but received two: %1$s";
    private static final String INFO_UPDATE_COUNT = "Update count %1$d: Statement: %2$s";
    private static final String INFO_DISPlAY_SINGLE_ROW_SINGLE_COLOUMN_INT_RESULT = "Result: %1$d SQL: %2$s";
    private static final String INFO_DISPLAY_SINGLE_COLUMN_RESULT_SET = "Results: %1$s SQL: %2$s";
    private static final String INFO_DDL_STATEMENT = "Executing DDL: %1$s";
    private static final String INFO_BATCH_CLEAN = "BATCH: %1$s CLEAN: %2$s";
    private static final String LIST_ALL_TABLES_QUERY = "SELECT DISTINCT s.schemaname||'.'||t.tablename FROM sys.systables as t, sys.sysschemas as s WHERE s.schemaid = t.schemaid";

    private static final String SQL_DRIVER_KEY = "DB_DRIVER_CLASS";
    private static final String DB_URL_KEY = "JDBC_URL";
    private static final String DB_USERNAME_KEY = "DB_USERNAME";
    private static final String DB_PASSWORD_KEY = "DB_PASSWORD";

    private Properties dbProperties;

    public void start() throws Exception {
        dbProperties = getDatabaseProperties();
        registerDriver(dbProperties);
    }

    public List<String> listAllTables() {
        try {
            return getSingleRowAsStringResultSetQuery(LIST_ALL_TABLES_QUERY);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public void create(boolean dropTableIfFound, boolean truncateTableIfFound, boolean populateStaticData, boolean populateTestData) {
        createTables(dropTableIfFound);
        if (populateStaticData) {
            populateTables(truncateTableIfFound);
        }
        if (populateTestData) {
            populateTablesWithTestData(false);
        }
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

    private boolean createTables(boolean dropTableIfFound) {
        boolean clean;
        CreateTableBatchCommand batch = new CreateTableBatchCommand(this, CREATE_TABLE_LOCATION, dropTableIfFound);
        try {
            clean = batch.prepare() && batch.execute();
            System.out.println(String.format(INFO_BATCH_CLEAN, "create tables", clean));
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return clean;
    }

    private boolean populateTables(boolean truncateDataIfFound) {
        boolean clean;
        InsertDataSqlBatchCommand batch = new InsertDataSqlBatchCommand(this, INSERT_TABLE_LOCATION, truncateDataIfFound);
        try {
            clean = batch.prepare() && batch.execute();
            System.out.println(String.format(INFO_BATCH_CLEAN, "insert data", clean));
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return clean;
    }

    private boolean populateTablesWithTestData(boolean truncateDataIfFound) {
        boolean clean;
        InsertDataSqlBatchCommand batch = new InsertDataSqlBatchCommand(this, INSERT_TABLE_TEST_DATA, truncateDataIfFound);
        try {
            clean = batch.prepare() && batch.execute();
            System.out.println(String.format(INFO_BATCH_CLEAN, "insert test data", clean));
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
        return clean;
    }

    public void executeDDLStatement(String sql) throws SQLException {
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                System.out.println(String.format(INFO_DDL_STATEMENT, sql));
                System.out.flush();
                if (statement.execute(sql)) {
                    throw new IllegalStateException(String.format(EXCEPTION_EXPECTED_UPDATE_COUNT_NOT_RESULT_SET, sql));
                }
                System.out.println(String.format(INFO_UPDATE_COUNT, statement.getUpdateCount(), sql));
            }
        }
    }

    public int executeSingleFieldSingleRowIntResultSetQuery(String sql) throws SQLException {
        int result;
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                if (!statement.execute(sql)) {
                    throw new IllegalStateException(String.format(EXCEPTION_EXPECTED_RESULT_SET_NOT_UPDATE_COUNT, sql));
                }
                try (ResultSet resultSet = statement.getResultSet()) {
                    if (!resultSet.next()) {
                        throw new IllegalStateException(String.format(EXCEPTION_EXPECTED_NON_ZERO_RESULT_SET, sql));
                    }
                    result = resultSet.getInt(1);
                    System.out.println(String.format(INFO_DISPlAY_SINGLE_ROW_SINGLE_COLOUMN_INT_RESULT, result, sql));
                    if (resultSet.next()) {
                        throw new IllegalStateException(String.format(EXCEPTION_EXPECTED_EXACTLY_ONE_RESULT_SET, sql));
                    }
                }
            }
        }
        return result;
    }

    public List<String> getSingleRowAsStringResultSetQuery(String sql) throws SQLException {
        List<String> results = new ArrayList<>();
        try (Connection connection = getConnection()) {
            try (Statement statement = connection.createStatement()) {
                if (!statement.execute(sql)) {
                    throw new IllegalStateException(String.format(EXCEPTION_EXPECTED_RESULT_SET_NOT_UPDATE_COUNT, sql));
                }
                ResultSet resultSet = statement.getResultSet();
                while ( resultSet.next() ) {
                    results.add(resultSet.getString(1));
                }
                System.out.println(String.format(INFO_DISPLAY_SINGLE_COLUMN_RESULT_SET, sql, results));
            }
        }
        return results;
    }
}
