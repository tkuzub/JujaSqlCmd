package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.Command;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Find implements Command {
    private final DatabaseManager manager;
    private final View view;

    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
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
}
