package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class MainController {
    private View view;
    private DatabaseManager manager;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public void run() {
        connectToDB();
        while (true) {
            view.write("Enter an existing command (or command 'help' for help)");
            String command = view.read();

            if (command.equals("tables")) {
                doTables();
            } else if (command.equals("help")) {
                doHelp();
            } else if (command.equals("exit")) {
                doExit();
            } else {
                view.write("non-existent command!!!");
            }
        }
    }

    private void doExit() {
        view.write("Good bay!!!");
        System.exit(0);
    }

    private void doHelp() {
        view.write("Existing teams: ");
        view.write("\thelp");
        view.write("\t\tfor to display all existing commands on the screen");
        view.write("\ttables");
        view.write("\t\tshow a list of all tables of the database to which they are connected");
        view.write("\texit");
        view.write("\t\tfor exit with database");
    }

    private void doTables() {
        String[] tableNames = manager.getTableNames();
        String message = Arrays.toString(tableNames);
        view.write(message);
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
