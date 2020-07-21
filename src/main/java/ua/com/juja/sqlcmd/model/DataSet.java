package ua.com.juja.sqlcmd.model;

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
    int freeIndex = 0;

    public void put(String name, Object value) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].getName().equals(name)) {
                data[index].value = value;
                return;
            }
        }
        data[freeIndex++] = new Data(name, value);
    }

    public Object[] getValues() {
        Object[] result = new Object[freeIndex];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getNames() {
        String[] result = new String[freeIndex];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    public Object get(String name) {
        for (int index = 0; index < freeIndex; index++) {
            if (data[index].getName().equals(name)) {
                return data[index].getValue();
            }
        }
        return null;
    }

    public void updateFrom(DataSet newValue) {
        for (int index = 0; index < newValue.freeIndex; index++) {
            Data data = newValue.data[index];
            this.put(data.name, data.value);
        }
    }

    @Override
    public String toString() {
        return "DataSet{\n" +
                "name "  + Arrays.toString(getNames()) + "\n" +
                "value " + Arrays.toString(getValues()) + "\n" +
                "}\n";
    }
}
