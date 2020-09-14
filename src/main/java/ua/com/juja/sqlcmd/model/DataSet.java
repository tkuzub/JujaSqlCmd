package ua.com.juja.sqlcmd.model;

public interface DataSet {
    void put(String name, Object value);

    Object[] getValues();

    String[] getNames();
}
