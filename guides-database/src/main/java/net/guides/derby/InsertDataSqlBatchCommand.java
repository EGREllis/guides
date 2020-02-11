package net.guides.derby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertDataSqlBatchCommand implements SqlCommand {
    private static final Pattern DAT_FILE_PATTERN = Pattern.compile("\\s*([\\S]+.sql)\\s*");
    private static final String INFO_VALIDATED_TABLE_TRUNCATED = "Confirmed no data in table %1$s";
    private static final String VALIDATE_DATA_WITHOUT_SCHEMA = "SELECT count(0) FROM %1$s";
    private static final String TRUNCATE_DATA_WITHOUT_SCHEMA = "TRUNCATE TABLE %1$s";

    private static final String INFO_VERIFIED_DATA_NOT_PRESENT = "Validated table is empty %1$s";
    private static final String WARNING_COULD_NOT_VALIDATE_TABLE = "Data found in table %1$s";
    private static final String WARNING_COULD_NOT_VALIDATE_TRUNCATING_TABLE = "Could not validate no data in table %1$s";
    private static final String VERIFY_DATA_INSERTED_QUERY = "SELECT count(0) FROM %1$s";
    private static final String INFO_VERIFIED_INSERT = "Verified %1$d rows inserted into table %2$s";
    private static final String WARNING_VERIFICATION_MISMATCH = "WARNING: Expected %1$d rows. Actual rows %2$d, table %3$s";
    private static final String DAT_FILE_ERROR_FORMAT = "Line in %1$s did not match expectations : %2$s";
    private static final String CREATE_TABLE_LOCATION_FORMAT = "dml/%1$s";

    private final Database database;
    private final String fileLocation;
    private final boolean truncateTableIfFound;
    private List<InsertDataSqlCommand> batchCommands;

    public InsertDataSqlBatchCommand(Database database, String fileLocation, boolean truncateTableIfFound) {
        this.database = database;
        this.fileLocation = fileLocation;
        this.truncateTableIfFound = truncateTableIfFound;
    }

    boolean read() {
        batchCommands = new ArrayList<>();
        try (InputStream fileStream = ClassLoader.getSystemResourceAsStream(fileLocation)) {
            List<InsertDataSqlCommand> insertCommand = getSqlCommandsFromDatFile(fileStream);
            batchCommands.addAll(insertCommand);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return true;
    }

    @Override
    public boolean prepare() throws SQLException {
        boolean safeToRun = read();
        if (!safeToRun) {
            return false;
        }
        Set<String> tables = new TreeSet<>();
        for (InsertDataSqlCommand command : batchCommands) {
            if (command.getSchemaName() != null) {
                tables.add(command.getSchemaName()+"."+command.getTableName());
            } else {
                tables.add(command.getTableName());
            }
        }
        for (String table : tables) {
            String tableCountSql = String.format(VALIDATE_DATA_WITHOUT_SCHEMA, table);
            int tableCount = database.executeSingleFieldSingleRowIntResultSetQuery(tableCountSql);
            if (tableCount == 0) {
                System.out.println(String.format(INFO_VERIFIED_DATA_NOT_PRESENT, table));
                safeToRun = true;
            } else if (tableCount == 1) {
                System.out.println(String.format(WARNING_COULD_NOT_VALIDATE_TABLE, table));
                if (truncateTableIfFound) {
                    String truncateSql = String.format(TRUNCATE_DATA_WITHOUT_SCHEMA, table);
                    database.executeDDLStatement(truncateSql);
                    int validateDroped = database.executeSingleFieldSingleRowIntResultSetQuery(tableCountSql);
                    if (validateDroped == 0) {
                        System.out.println(String.format(INFO_VALIDATED_TABLE_TRUNCATED, table));
                        safeToRun = true;
                    } else {
                        System.err.println(String.format(WARNING_COULD_NOT_VALIDATE_TRUNCATING_TABLE, table));
                    }
                }
            }
        }
        return safeToRun;
    }

    @Override
    public boolean execute() throws SQLException {
        boolean clean = true;
        for (int i = 0; i < batchCommands.size() && clean; i++) {
            SqlCommand command = batchCommands.get(i);
            clean = command.execute();
        }
        return clean && verifyBatch();
    }

    private List<InsertDataSqlCommand> getSqlCommandsFromDatFile(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        InsertDataSqlCommandParser parser;
        String line;
        List<InsertDataSqlCommand> sqlCommands = new ArrayList<>();
        while ( ((line = reader.readLine()) != null)) {
            Matcher matcher = DAT_FILE_PATTERN.matcher(line);
            if (matcher.matches()) {
                String file = matcher.group(1);
                String location = String.format(CREATE_TABLE_LOCATION_FORMAT, file);
                parser = new InsertDataSqlCommandParser(database, location);
                sqlCommands = parser.parse();
            } else {
                System.err.println(String.format(DAT_FILE_ERROR_FORMAT, fileLocation, line));
            }
        }
        return sqlCommands;
    }

    private boolean verifyBatch() throws SQLException {
        Map<String,Integer> expectedData = new HashMap<>();
        for (InsertDataSqlCommand insert : batchCommands) {
            String key;
            if (insert.getSchemaName() != null) {
                key = insert.getSchemaName()+"."+insert.getTableName();
            } else {
                key = insert.getTableName();
            }
            if (!expectedData.containsKey(key)) {
                expectedData.put(key, 1);
            } else {
                expectedData.put(key, expectedData.get(key)+1);
            }
        }
        boolean clean = true;
        for (Map.Entry<String,Integer> entry : expectedData.entrySet()) {
            String verifySql = String.format(VERIFY_DATA_INSERTED_QUERY, entry.getKey());
            int nRows = database.executeSingleFieldSingleRowIntResultSetQuery(verifySql);
            if (clean && nRows != entry.getValue()) {
                System.err.println(String.format(WARNING_VERIFICATION_MISMATCH, entry.getValue(), nRows, entry.getKey()));
                clean = false;
            } else {
                System.out.println(String.format(INFO_VERIFIED_INSERT, nRows, entry.getKey()));
            }
        }
        return clean;
    }
}
