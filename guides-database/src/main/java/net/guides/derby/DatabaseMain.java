package net.guides.derby;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMain {
    public static void main(String args[]) throws Exception {
        List<String> arguments = new ArrayList<>();
        for (String arg : args) {
            arguments.add(arg);
        }
        boolean dropTableIfFound = arguments.contains("--drop");
        boolean truncateDateIfFound = arguments.contains("--truncate");
        boolean populateStaticData = arguments.contains("--populate");
        boolean populateTestData = arguments.contains("--test");
        Database database = new Database();
        database.start();
        database.create(dropTableIfFound, truncateDateIfFound, populateStaticData, populateTestData);
    }
}
