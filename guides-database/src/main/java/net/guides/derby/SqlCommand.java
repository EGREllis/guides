package net.guides.derby;

import java.sql.SQLException;

public interface SqlCommand {
    boolean execute() throws SQLException;
}
