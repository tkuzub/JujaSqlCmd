package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.LinkedList;
import java.util.List;

public class Create implements Command {
    private final DatabaseManager manager;
    private final View view;


    public Create(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("create|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("you entered the wrong number of parameters in the format " +
                    "expected 'create|tableName|column1|column2|...|columnN'" +
                    " but you entered " + command);
        }
        String tableName = data[1];
        List<String> input = new LinkedList<>();
        for (int index = 2; index < data.length; index++) {
            input.add(data[index]);
        }
        manager.create(tableName, input);
        view.write(String.format("the table '%s' was created successfully", tableName));
    }
}
