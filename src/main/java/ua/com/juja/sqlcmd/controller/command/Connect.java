package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Connect implements Command {

    public static final String COMMAND_SAMPLE = "connect|sqlcmd_db|postgres|777";
    private final DatabaseManager manager;
    private final View view;

    public Connect(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("connect|");
    }

    @Override
    public void process(String command) {
        try {
            String[] data = command.split("\\|");
            if (data.length != count()) {
                throw new IllegalArgumentException(String.format(
                        "incorrect number of entered parameters separated by '|' expected %s, " +
                                "but entered %s", count(), data.length)
                );
            }
            String databaseName = data[1];
            String userName = data[2];
            String password = data[3];

            manager.connect(databaseName, userName, password);
            view.write("Success!!!");
        } catch (Exception e) {
            printError(e);
        }
    }

    public int count() {
        return COMMAND_SAMPLE.split("\\|").length;
    }

    private void printError(Exception e) {
        String massage = e.getMessage();
        if (e.getCause() != null) {
            massage += " " + e.getCause().getMessage();
        }
        view.write("FAIL for a cause " + massage);
        view.write("Try again!!!");
    }
}