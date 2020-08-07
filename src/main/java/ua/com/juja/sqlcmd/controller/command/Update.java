package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class Update implements Command {
    private final DatabaseManager manager;
    private final View view;

    public Update(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("update|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("\\|");
        String tableName = data[1];
        int id = Integer.parseInt(data[3]);
        DataSet newValue = new DataSet();
        if (data.length % 2 != 0) {
            throw new IllegalArgumentException("you entered the wrong number of parameters in the format" +
                    "expected 'update|tableName|column1|value1|column2|value2'" +
                    " but you entered " + command);
        }

        for (int index = 4; index < data.length; index += 2) {
            String columnName = data[index];
            String value = data[index + 1];
            newValue.put(columnName, value);
        }

        manager.update(tableName, id, newValue);

        String[] tableColumns = manager.getTableColumns(tableName);
        DataSet[] tableData = manager.getTableData(tableName);

        view.write("===================");
        printHeader(tableColumns);
        view.write("===================");
        printTable(tableData);
    }

    private void printTable(DataSet[] tableData) {
        for (DataSet data : tableData) {
            printRow(data);
        }
    }

    private void printRow(DataSet data) {
        Object[] values = data.getValues();
        StringBuilder result = new StringBuilder("|");
        for (Object value : values) {
            result.append(value).append("|");
        }
        view.write(result.toString());
    }

    private void printHeader(String[] tableColumns) {
        StringBuilder result = new StringBuilder("|");
        for (String column : tableColumns) {
            result.append(column).append("|");
        }
        view.write(result.toString());
    }
}
