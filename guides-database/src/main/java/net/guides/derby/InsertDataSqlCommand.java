package net.guides.derby;

import java.sql.SQLException;

public class InsertDataSqlCommand implements SqlCommand {
    private final Database database;
    private final String insertSql;
    private final String tableName;
    private final String schemaName;

    public InsertDataSqlCommand(Database database, String schemaName, String tableName, String insertSql) {
        this.database = database;
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.insertSql = insertSql;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public boolean prepare() {
        return true;
    }

    @Override
    public boolean execute() throws SQLException {
        return prepare() && perform() && validate();
    }

    boolean perform() throws SQLException {
        database.executeDDLStatement(insertSql);
        return true;
    }

    boolean validate() {
        return true;
    }
}
