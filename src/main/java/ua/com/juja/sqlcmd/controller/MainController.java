package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Exit;
import ua.com.juja.sqlcmd.controller.command.Help;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class MainController {
    private final View view;
    private final DatabaseManager manager;
    private final Command[] commands;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{
                new Tables(manager, view),
                new Help(view),
                new Exit(view),
                new Find(manager, view)};
    }

    public void run() {
        connectToDB();
        while (true) {
            view.write("Enter an existing command (or command 'help' for help)");
            String command = view.read();

            if (commands[0].canProcess(command)) {
                commands[0].process(command);
            } else if (commands[1].canProcess(command)) {
                commands[1].process(command);
            } else if (commands[2].canProcess(command)) {
                commands[2].process(command);
            } else if (commands[3].canProcess(command)) {
                commands[3].process(command);
            } else {
                view.write("non-existent command!!!");
            }
        }
    }

    private void connectToDB() {
        view.write("Hello user!!!");
        view.write("Please enter the database name, username and password in the format databaseName|userName|password");

        while (true) {
            try {
                String string = view.read();
                String[] data = string.split("\\|");
                if (data.length != 3) {
                    throw new IllegalArgumentException(
                            "incorrect number of entered parameters separated by '|' expected 3, but entered " + data.length);
                }
                String databaseName = data[0];
                String userName = data[1];
                String password = data[2];

                manager.connect(databaseName, userName, password);
                break;
            } catch (Exception e) {
                printError(e);
            }
        }
        view.write("Success!!!");
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
