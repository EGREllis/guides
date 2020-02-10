package net.guides.derby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTableSqlCommand implements SqlCommand {
    private static final Pattern CREATE_TABLE_PATTERN = Pattern.compile("(CREATE TABLE\\s*([\\.\\w]+)\\s*[^;]+);");
    private static final String EXCEPTION_SQL_STATEMENT_DID_NOT_MATCH = "File: %1$s \tStatement did not match validation pattern";
    private static final String EXCEPTION_EXPECTED_RESULT_SET_NOT_UPDATE_COUNT = "Expected to execute a query returning a result set, but received an update count instead: %1$s";
    private static final String EXCEPTION_EXPECTED_UPDATE_COUNT_NOT_RESULT_SET = "Expected to execute a query returning an update count, but received a result set instead: %1$s";
    private static final String EXCEPTION_EXPECTED_NON_ZERO_RESULT_SET = "Expected at least one result set: %1$s";
    private static final String EXCEPTION_EXPECTED_EXACTLY_ONE_RESULT_SET = "Expected exactly one result set but received two: %1$s";
    private static final String WARNING_FOUND_TABLE = "Warning: Found table %1$s";
    private static final String WARNING_COULD_NOT_VALIDATE_CREATING_TABLE = "Warning: Could not validate table %1$s";
    private static final String WARNING_COULD_NOT_VALIDATE_DROPPING_TABLE = "Warning: Could not verify dropping table %1$s";
    private static final String INFO_UPDATE_COUNT = "Update count %1$d: Statement: %2$s";
    private static final String INFO_VALIDATED_TABLE = "Validated creating table %1$s";
    private static final String INFO_VALIDATED_TABLE_DROPPED = "Validated dropping table %1$s";
    private static final String INFO_VERIFIED_TABLE_DOES_NOT_EXIST = "Verified table does not exist: %1$s";
    private static final String INFO_DISPlAY_SINGLE_ROW_SINGLE_COLOUMN_INT_RESULT = "Result: %1$d SQL: %2$s";
    private static final String INFO_DDL_STATEMENT = "Executing DDL: %1$s";
    private static final String VALIDATE_TABLE_NO_SCHEMA_EXISTS_STATEMENT_FORMAT = "SELECT count(0) as value FROM sys.systables WHERE tablename = '%1$s'";
    private static final String VALIDATE_TABLE_WITH_SCHEMA_EXISTS_STATEMENT_FORMAT = "SELECT count(0) as value FROM sys.systables t, sys.sysschemas s WHERE t.tablename = '%1$s' AND s.schemaid = t.schemaid AND s.schemaname = '%2$s'";
    private static final String DROP_TABLE_STATEMENT_NO_SCHEMA_FORMAT = "DROP TABLE %1$s";
    private static final String DROP_TABLE_STATEMENT_WITH_SCHEMA_FORMAT = "DROP TABLE %1$s.%2$s";

    private final Database database;
    private final String classPathLocation;
    private final boolean dropIfFound;
    private String createSql;
    private String tableName;
    private String schemaName;

    public CreateTableSqlCommand(Database database, String classPathLocation, boolean dropIfFound) {
        this.database = database;
        this.classPathLocation = classPathLocation;
        this.dropIfFound = dropIfFound;
    }

    @Override
    public boolean execute() throws SQLException {
        return read() && prepare() && perform() && validate();
    }

    int executeSingleFieldSingleRowIntResultSetQuery(String sql) throws SQLException {
        int result;
        try (Connection connection = database.getConnection()) {
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

    void executeDDLStatement(String sql) throws SQLException {
        try (Connection connection = database.getConnection()) {
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

    boolean read() {
        try (InputStream fileStream = ClassLoader.getSystemResourceAsStream(classPathLocation)) {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));
            String fileLine;
            StringBuilder sqlBuilder = new StringBuilder();
            while ( (fileLine = fileReader.readLine()) != null ) {
                sqlBuilder.append(fileLine);
            }
            String sql = sqlBuilder.toString();
            Matcher statementMatcher = CREATE_TABLE_PATTERN.matcher(sql);
            if (statementMatcher.matches()) {
                createSql = statementMatcher.group(1);
                tableName = statementMatcher.group(2).toUpperCase();
                if (tableName.contains(".")) {
                    String split[] = tableName.split("\\.");
                    schemaName = split[0];
                    tableName = split[1];
                }
            } else {
                throw new IOException(String.format(EXCEPTION_SQL_STATEMENT_DID_NOT_MATCH, classPathLocation));
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return true;
    }

    private String getValidateTableSql() {
        String tableCountSql;
        if (schemaName != null) {
            tableCountSql = String.format(VALIDATE_TABLE_WITH_SCHEMA_EXISTS_STATEMENT_FORMAT, tableName, schemaName);
        } else {
            tableCountSql = String.format(VALIDATE_TABLE_NO_SCHEMA_EXISTS_STATEMENT_FORMAT, tableName);
        }
        return tableCountSql;
    }

    private String getDropTableSql() {
        String dropTableSql;
        if (schemaName != null) {
            dropTableSql = String.format(DROP_TABLE_STATEMENT_WITH_SCHEMA_FORMAT, schemaName, tableName);
        } else {
            dropTableSql = String.format(DROP_TABLE_STATEMENT_NO_SCHEMA_FORMAT, tableName);
        }
        return dropTableSql;
    }

    public boolean prepare() throws SQLException {
        if (!read()) {
            return false;
        }
        boolean safeToRun = false;
        String tableCountSql = getValidateTableSql();
        int tableCount = executeSingleFieldSingleRowIntResultSetQuery(tableCountSql);
        if (tableCount == 0) {
            System.out.println(String.format(INFO_VERIFIED_TABLE_DOES_NOT_EXIST, tableName));
            safeToRun = true;
        } else if (tableCount == 1) {
            System.out.println(String.format(WARNING_FOUND_TABLE, tableName));
            if (dropIfFound) {
                String dropTable = getDropTableSql();
                executeDDLStatement(dropTable);
                int validateDroped = executeSingleFieldSingleRowIntResultSetQuery(tableCountSql);
                if (validateDroped == 0) {
                    System.out.println(String.format(INFO_VALIDATED_TABLE_DROPPED, tableName));
                    safeToRun = true;
                } else {
                    System.err.println(String.format(WARNING_COULD_NOT_VALIDATE_DROPPING_TABLE, tableName));
                }
            }
        }
        return safeToRun;
    }

    boolean perform() throws SQLException {
        executeDDLStatement(createSql);
        return true;
    }

    boolean validate() throws SQLException {
        boolean result = false;
        String tableQuery = String.format(VALIDATE_TABLE_NO_SCHEMA_EXISTS_STATEMENT_FORMAT, tableName);
        int tableCount = executeSingleFieldSingleRowIntResultSetQuery(tableQuery);
        if (tableCount == 1) {
            result = true;
            System.out.println(String.format(INFO_VALIDATED_TABLE, tableName));
        } else {
            System.err.println(String.format(WARNING_COULD_NOT_VALIDATE_CREATING_TABLE, tableName));
        }
        return result;
    }
}
