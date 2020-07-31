package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.controller.command.Exit;
import ua.com.juja.sqlcmd.controller.command.Help;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;

public class MainController {
    private View view;
    private DatabaseManager manager;
    private Command[] commands;

    public MainController(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        this.commands = new Command[]{new Exit(view), new Help(view)};
    }

    public void run() {
        connectToDB();
        while (true) {
            view.write("Enter an existing command (or command 'help' for help)");
            String command = view.read();

            if (command.equals("tables")) {
                doTables();
            } else if (commands[1].canProcess(command)) {
                commands[1].process(command);
            } else if (command.startsWith("find|")) {
                doFind(command);
            } else if (commands[0].canProcess(command)) {
                commands[0].process(command);
            } else {
                view.write("non-existent command!!!");
            }
        }
    }

    private void doFind(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];

        DataSet[] tableData = manager.getTableData(tableName);
        String[] tableColumns = manager.getTableColumns(tableName);
        view.write("===================");
        printHeader(tableColumns);
        view.write("===================");
        printTable(tableData);
    }

    private void printTable(DataSet[] tableData) {

        for (DataSet row : tableData) {
            printRow(row);
        }
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        StringBuilder result = new StringBuilder("|");
        for (Object value : values) {
            result.append(value).append("|");
        }
        view.write(result.toString());
    }

    private void printHeader(String[] tableColumns) {
        StringBuilder result = new StringBuilder("|");
        for (String name : tableColumns) {
            result.append(name).append("|");
        }
        view.write(result.toString());
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
