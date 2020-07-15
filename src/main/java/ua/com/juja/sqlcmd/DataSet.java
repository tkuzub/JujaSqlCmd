package ua.com.juja.sqlcmd;

import java.util.Arrays;

public class DataSet {
    static class Data {
        private String name;
        private Object value;

        public Data(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }

    Data[] data = new Data[100];
    int index = 0;
    public void put(String name, Object value) {
        data[index++] = new Data(name, value);
    }

    public Object[] getValues() {
        Object[] result = new Object[index];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames() {
        String[] result = new String[index];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "name "  + Arrays.toString(getNames()) + "\n" +
                "value " + Arrays.toString(getValues()) + "\n" +
                "}\n";
    }
}
