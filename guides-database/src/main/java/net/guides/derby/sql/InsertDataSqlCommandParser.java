package net.guides.derby.sql;

import net.guides.derby.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertDataSqlCommandParser {
    private static final Pattern INSERT_TABLE_PATTERN = Pattern.compile("(INSERT INTO\\s*([\\.\\w]+)\\s*[^;]+)+");
    private static final String EXCEPTION_SQL_STATEMENT_DID_NOT_MATCH = "File: %1$s \tStatement did not match validation pattern: %2$s";

    private final Database database;
    private final String fileLocation;

    public InsertDataSqlCommandParser(Database database, String fileLocation) {
        this.database = database;
        this.fileLocation = fileLocation;
    }

    public List<InsertDataSqlCommand> parse() {
        List<InsertDataSqlCommand> results = new ArrayList<>();
        try (InputStream fileStream = ClassLoader.getSystemResourceAsStream(fileLocation)) {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));
            String fileLine;
            StringBuilder sqlBuilder = new StringBuilder();
            while ( (fileLine = fileReader.readLine()) != null ) {
                sqlBuilder.append(fileLine);
            }
            String sql = sqlBuilder.toString();
            String statements[] = sql.split(";");
            for (String statement : statements) {
                Matcher statementMatcher = INSERT_TABLE_PATTERN.matcher(statement);
                if (statementMatcher.matches()) {
                    String insertSql = statementMatcher.group(1);
                    String tableName = statementMatcher.group(2).toUpperCase();
                    String schemaName = null;
                    
                    if (tableName.contains(".")) {
                        String split[] = tableName.split("\\.");
                        schemaName = split[0];
                        tableName = split[1];
                    }
                    results.add(new InsertDataSqlCommand(database, schemaName, tableName, insertSql));
                } else {
                    throw new IOException(String.format(EXCEPTION_SQL_STATEMENT_DID_NOT_MATCH, fileLocation, statement));
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return results;
    }
}
