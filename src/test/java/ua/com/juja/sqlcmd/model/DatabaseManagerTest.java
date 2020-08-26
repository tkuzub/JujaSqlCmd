package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class DatabaseManagerTest {
    private DatabaseManager manager;

    protected abstract DatabaseManager getDatabaseManager();

    @Before
    public void setup() {
        manager = getDatabaseManager();
        manager.connect("sqlcmd_db", "postgres", "777");
    }

    @Test
    public void testGetAllTableName() {
        String[] tableNames = manager.getTableNames();
        assertEquals("[user_info, test]", Arrays.toString(tableNames));
    }

    @Test
    public void testGetTableData() {
        //given
        manager.clear("user_info");
        //when
        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven Pupkin");
        input.put("password", "password");
        manager.insert("user_info", input);
        //then
        DataSet[] users = manager.getTableData("user_info");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[id, name, password]", Arrays.toString(user.getNames()));
        assertEquals("[13, Stiven Pupkin, password]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testUpdateTableData() {
        //given
        manager.clear("user_info");
        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven Pupkin");
        input.put("password", "password");
        manager.insert("user_info", input);
        //when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");

        DataSet checkData = new DataSet();
        checkData.put("id", 13);
        manager.update("user_info", checkData, newValue);
        //then
        DataSet[] users = manager.getTableData("user_info");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[id, name, password]", Arrays.toString(user.getNames()));
        assertEquals("[13, Stiven Pupkin, pass2]", Arrays.toString(user.getValues()));
    }

    @Test
    public void testDeleteEntryFromTable() {
        //given
        manager.clear("user_info");
        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven Pupkin");
        input.put("password", "password");
        manager.insert("user_info", input);

        input.put("id", 14);
        input.put("name", "Bob Jons");
        input.put("password", "anaconda");
        manager.insert("user_info", input);

        DataSet[] users = manager.getTableData("user_info");
        assertEquals(2, users.length);

        DataSet user1 = users[0];
        assertEquals("[id, name, password]", Arrays.toString(user1.getNames()));
        assertEquals("[13, Stiven Pupkin, password]", Arrays.toString(user1.getValues()));

        DataSet user2 = users[1];
        assertEquals("[id, name, password]", Arrays.toString(user2.getNames()));
        assertEquals("[14, Bob Jons, anaconda]", Arrays.toString(user2.getValues()));

        //when
        DataSet deleteData = new DataSet();
        deleteData.put("name", "Bob Jons");

        //then
        manager.delete("user_info", deleteData);
        users = manager.getTableData("user_info");
        assertEquals(1, users.length);
    }

    @Test
    public void testCreateTable() {
        //given
        List<String> input = Arrays.asList("name", "password");
        String tableName = "some_table";
        //when
        manager.create(tableName, input);
        //then
        String[] tableNames = manager.getTableNames();
        assertEquals("[user_info, test, some_table]", Arrays.toString(tableNames));

        String[] tableColumns = manager.getTableColumns(tableName);
        assertEquals("[id, name, password]", Arrays.toString(tableColumns));

        //delete table after create
        manager.drop(tableName);
    }

    @Test
    public void testGetTableColumns() {
        //given
        manager.clear("user_info");
        //when
        String[] data = manager.getTableColumns("user_info");
        //then
        assertEquals("[id, name, password]", Arrays.toString(data));
    }

    @Test
    public void testIsConnected() {
        assertTrue(manager.isConnected());
    }

    @Test
    public void testClearTableName() {
        //given
        manager.clear("user_info");
        DataSet input = new DataSet();
        input.put("id", 13);
        input.put("name", "Stiven Pupkin");
        input.put("password", "password");

        manager.insert("user_info", input);
        DataSet[] users = manager.getTableData("user_info");

        assertEquals(1, users.length);
        DataSet user = users[0];
        assertEquals("[id, name, password]", Arrays.toString(user.getNames()));
        assertEquals("[13, Stiven Pupkin, password]", Arrays.toString(user.getValues()));

        //then
        manager.clear("user_info");
        DataSet[] dataAfterClear = manager.getTableData("user_info");
        //when
        assertEquals(0, dataAfterClear.length);
    }
}
