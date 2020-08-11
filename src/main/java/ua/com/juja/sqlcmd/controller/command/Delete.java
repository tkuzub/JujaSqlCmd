package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Delete implements Command {
    private final DatabaseManager manager;
    private final View view;

    public Delete(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("delete|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("you entered the wrong number of parameters in the format" +
                    "expected 'update|tableName|column1|value1|column2|value2'" +
                    " but you entered " + command);
        }
        String tableName = data[1];
        DataSet input = new DataSet();

        manager.delete(tableName, input);
    }
}
