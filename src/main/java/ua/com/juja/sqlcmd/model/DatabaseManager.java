package ua.com.juja.sqlcmd.model;

import java.util.List;

public interface DatabaseManager {
    DataSet[] getTableData(String tableName);

    String[] getTableNames();

    void connect(String database, String user, String password);

    void clear(String tableName);

    void insert(String tableName, DataSet input);

    void update(String tableName, DataSet checkData, DataSet newValue);

    String[] getTableColumns(String tableName);

    boolean isConnected();

    void drop(String tableName);

    void create(String tableName, List<String> input);

    void delete(String tableName, DataSet input);
}
