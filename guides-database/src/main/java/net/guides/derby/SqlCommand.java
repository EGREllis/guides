package net.guides.derby;

import java.sql.SQLException;

public interface SqlCommand {
    boolean prepare() throws SQLException;
    boolean execute() throws SQLException;
}
