package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Insert implements Command {

    private final DatabaseManager manager;
    private final View view;

    public Insert(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("insert|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("you entered the wrong number of parameters in the format" +
                            "expected 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN'" +
                            " but you entered " + command);
        }

        String tableName = data[1];
        DataSet input = new DataSet();

        for (int index = 1; index < data.length / 2; index++) {
            String column = data[index * 2];
            String value = data[index * 2 + 1];
            input.put(column, value);
        }
        manager.insert(tableName, input);

        view.write(String.format("new data has been added to the '%s' ", tableName));
    }
}
