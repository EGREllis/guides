package net.guides.derby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTableBatchCommand implements SqlCommand {
    private static final Pattern DAT_FILE_PATTERN = Pattern.compile("\\s*([\\S]+.sql)\\s*");
    private static final String VERIFY_TABLES_QUERY_FORMAT = "SELECT t.tablename FROM sys.systables as t, sys.sysschemas as s WHERE s.schemaid = t.schemaid AND s.schemaname = '%1$s'";
    private static final String VERIFY_SCHEMA = "SELECT schemaname FROM sys.sysschemas";
    private static final String CREATE_TABLE_LOCATION_FORMAT = "ddl/%1$s";
    private static final String DAT_FILE_ERROR_FORMAT = "Line in %1$s did not match expectations : %2$s";
    private final Database database;
    private final String createTableLocation;
    private final boolean dropTableIfFound;
    private List<CreateTableSqlCommand> batchCommands;

    public CreateTableBatchCommand(Database database, String createTableLocation, boolean dropTableIfFound) {
        this.database = database;
        this.createTableLocation = createTableLocation;
        this.dropTableIfFound = dropTableIfFound;
    }

    @Override
    public boolean prepare() throws SQLException {
        boolean clean = true;
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(createTableLocation)) {
            batchCommands = getSqlCommandsFromDatFile(inputStream);
            for (int i = batchCommands.size()-1; i > 0 && clean; i--) {
                SqlCommand command = batchCommands.get(i);
                clean = command.prepare();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return clean;
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

    private List<CreateTableSqlCommand> getSqlCommandsFromDatFile(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        List<CreateTableSqlCommand> sqlCommands = new ArrayList<>();
        while ( ((line = reader.readLine()) != null)) {
            Matcher matcher = DAT_FILE_PATTERN.matcher(line);
            if (matcher.matches()) {
                String file = matcher.group(1);
                String location = String.format(CREATE_TABLE_LOCATION_FORMAT, file);
                CreateTableSqlCommand command = new CreateTableSqlCommand(database, location, dropTableIfFound);
                sqlCommands.add(command);
            } else {
                System.err.println(String.format(DAT_FILE_ERROR_FORMAT, createTableLocation, line));
            }
        }
        return sqlCommands;
    }

    private boolean verifyBatch() throws SQLException {
        List<String> tablesExpectedNotCreated = new ArrayList<>();
        List<String> tablesPresentNotExpected = new ArrayList<>();
        Map<String,Set<String>> schemaMap = new HashMap<>();
        for (CreateTableSqlCommand command : batchCommands) {
            String schemaName= command.getSchemaName();
            if (schemaName != null) {
                if (!schemaMap.containsKey(command.getSchemaName())) {
                    schemaMap.put(schemaName, new HashSet<String>());
                }
                schemaMap.get(schemaName).add(command.getTableName());
            }
        }
        database.getSingleRowAsStringResultSetQuery(VERIFY_SCHEMA);
        for (Map.Entry<String, Set<String>> entry : schemaMap.entrySet()) {
            String verifyTableQuey = String.format(VERIFY_TABLES_QUERY_FORMAT, entry.getKey());
            List<String> tables = database.getSingleRowAsStringResultSetQuery(verifyTableQuey);

            for (String createTable : entry.getValue()) {
                if (!tables.contains(createTable)) {
                    tablesExpectedNotCreated.add(createTable);
                }
            }
            for (String tablesPresent: tables) {
                if (!entry.getValue().contains(tablesPresent)) {
                    tablesPresentNotExpected.add(tablesPresent);
                }
            }
        }
        return tablesExpectedNotCreated.size() == 0 && tablesPresentNotExpected.size() == 0;
    }
}
