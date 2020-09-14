package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Set;

public class Tables implements Command {

    private final View view;
    private final DatabaseManager manager;

    public Tables(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.equals("tables");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length != 1) {
            throw new IllegalArgumentException("command format 'tables' but you entered: " + command);
        }
        Set<String> tableNames = manager.getTableNames();
        String message = tableNames.toString();
        view.write(message);
    }
}
