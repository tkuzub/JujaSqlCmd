package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
        if (data.length  != 4) {
            throw new IllegalArgumentException("you entered the wrong number of parameters in the format" +
                    "expected 'delete|tableName|column|value'" +
                    " but you entered " + command);
        }
        String tableName = data[1];
        DataSet deleteData = new DataSet();

        String columnName = data[2];
        String value = data[3];

        if (columnName.equals("id")) {
            int valueInt = Integer.parseInt(value);
            deleteData.put(columnName, valueInt);
        } else {
            deleteData.put(columnName, value);
        }
        manager.delete(tableName, deleteData);

        Set<String> tableColumns = manager.getTableColumns(tableName);
        List<DataSet> tableData = manager.getTableData(tableName);

        view.write("===================");
        printHeader(tableColumns);
        view.write("===================");
        printTable(tableData);
    }

    private void printHeader(Set<String> tableColumns) {
        StringBuilder result = new StringBuilder("|");
        for (String columnsName : tableColumns) {
            result.append(columnsName).append("|");
        }
        view.write(result.toString());
    }

    private void printTable(List<DataSet> tableData) {
        for (DataSet data : tableData) {
            printRow(data);
        }
    }

    private void printRow(DataSet data) {
        Object[] values = data.getValues();
        StringBuilder result = new StringBuilder("|");
        for(Object value : values) {
            result.append(value).append("|");
        }
        view.write(result.toString());
    }
}
