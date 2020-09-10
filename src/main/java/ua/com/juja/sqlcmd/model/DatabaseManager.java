package ua.com.juja.sqlcmd.model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    DataSet[] getTableData(String tableName);

    Set<String> getTableNames();

    void connect(String database, String user, String password);

    void clear(String tableName);

    void insert(String tableName, DataSet input);

    void update(String tableName, DataSet checkData, DataSet newValue);

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    void drop(String tableName);

    void create(String tableName, List<String> input);

    void delete(String tableName, DataSet input);
}
