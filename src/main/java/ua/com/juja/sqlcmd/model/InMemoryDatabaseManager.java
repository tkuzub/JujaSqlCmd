package ua.com.juja.sqlcmd.model;

import java.util.Arrays;

public class InMemoryDatabaseManager implements DatabaseManager {
    public static final String TABLE_NAME = "user_info";
    private DataSet[] data = new DataSet[1000];
    private int freeIndex = 0;

    @Override
    public DataSet[] getTableData(String tableName) {
        validateTableName(tableName);
        return Arrays.copyOf(data, freeIndex);
    }

    private void validateTableName(String tableName) {
        if (!("user_info".equals(tableName))) {
            throw new UnsupportedOperationException(
                    "Only work for 'user_info' but you want work with table " + tableName);
        }
    }

    @Override
    public String[] getTableNames() {
        return new String[] {TABLE_NAME, "test"};
    }

    @Override
    public void connect(String database, String user, String password) {
        //do nothing
    }

    @Override
    public void clear(String tableName) {
        validateTableName(tableName);
        data = new DataSet[1000];
        freeIndex = 0;
    }

    @Override
    public void create(String tableName, DataSet input) {
        validateTableName(tableName);
        data[freeIndex++] = input;
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        validateTableName(tableName);
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].get("id").equals(id)) {
                data[index].updateFrom(newValue);
            }
        }
    }

    @Override
    public String[] getTableColumns(String tableName) {
        return new String[]{"id, name, password"};
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}
