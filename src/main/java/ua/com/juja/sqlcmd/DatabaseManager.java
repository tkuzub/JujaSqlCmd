package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

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
            int size = rsCount.getInt(1);

            return size;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String[] getTableNames() {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type = 'BASE TABLE';")) {

            List<String> listTables = new ArrayList<>();
            while (rs.next()) {
                listTables.add(rs.getString("table_name"));
            }
            String[] tables = listTables.toArray(new String[0]);
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public void connect(String database, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Please add maven dependency in project");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/" + database, user, password);
        } catch (SQLException e) {
            System.out.println(String.format("Can get connection for databace:%s, user:%s, password:%s", database, user, password));
            e.printStackTrace();
            connection = null;
        }
    }

    public void clear(String tableName) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE from public." + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(String tableName ,DataSet input) {
        try (Statement stmt = connection.createStatement();) {

            String tableNames = getNameFormatted(input, "%s,");
            String value = getValueFormatted(input, "'%s',");

            stmt.executeUpdate("INSERT INTO public." + tableName + "(" + tableNames + ")"
                    + "VALUES (" + value + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getValueFormatted(DataSet input, String format) {
        String value = "";
        for (Object values : input.getValues()) {
            value += String.format(format, values);
        }
        value = value.substring(0, value.length() - 1);
        return value;
    }

    public String getNameFormatted(DataSet input, String format) {
        String tableNames = "";
        for (String name : input.getNames()) {
            tableNames += String.format(format, name);
        }
        tableNames = tableNames.substring(0, tableNames.length() - 1);
        return tableNames;
    }

    public void update(String tableName ,int id, DataSet newValue) {

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
