package ua.com.juja.sqlcmd.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCDatabaseManager implements DatabaseManager {
    private Connection connection;

    @Override
    public DataSet[] getTableData(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM public." + tableName)) {

            int size = getSize(tableName);
            ResultSetMetaData metaData = rs.getMetaData();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    dataSet.put(metaData.getColumnName(i), rs.getObject(i));
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return new DataSet[0];
        }
    }

    public int getSize(String tableName) {
        try (Statement stmt = connection.createStatement();
             ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM public." + tableName + ";")) {
            rsCount.next();
            return rsCount.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String[] getTableNames() {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type = 'BASE TABLE';")) {

            List<String> listTables = new ArrayList<>();
            while (rs.next()) {
                listTables.add(rs.getString("table_name"));
            }
            return listTables.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public void connect(String database, String userName, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("please add maven dependency in project", e);
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + database, userName, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(String.format(
                    "can get connection for databace:%s, userName:%s, password:%s", database, userName, password), e);
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE from public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, DataSet input) {
        try (Statement stmt = connection.createStatement()) {
            String tableNames = getNameFormatted(input, "%s,");
            String value = getValueFormatted(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + "(" + tableNames + ")"
                    + "VALUES (" + value + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getValueFormatted(DataSet input, String format) {
        StringBuilder value = new StringBuilder();
        for (Object values : input.getValues()) {
            value.append(String.format(format, values));
        }
        value = new StringBuilder(value.substring(0, value.length() - 1));
        return new String(value);
    }

    public String getNameFormatted(DataSet input, String format) {
        StringBuilder tableNames = new StringBuilder();
        for (String name : input.getNames()) {
            tableNames.append(String.format(format, name));
        }
        tableNames = new StringBuilder(tableNames.substring(0, tableNames.length() - 1));
        return new String(tableNames);
    }

    @Override
    public void update(String tableName, int id, DataSet newValue) {
        String tableNames = getNameFormatted(newValue, "%s = ?,");

        try (PreparedStatement pstmt = connection.prepareStatement("UPDATE public." + tableName
                + " SET " + tableNames
                + "WHERE id = ?")){
            int index = 1;
            for (Object value : newValue.getValues()) {
                pstmt.setObject(index++, value);
            }
            pstmt.setObject(index, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
