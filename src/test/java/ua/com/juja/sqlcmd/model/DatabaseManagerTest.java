package ua.com.juja.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

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
        manager.create("user_info" ,input);
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
        manager.create("user_info" , input);

        //when
        DataSet newValue = new DataSet();
        newValue.put("password", "pass2");
        manager.update("user_info" ,13, newValue);

        //then
        DataSet[] users = manager.getTableData("user_info");
        assertEquals(1, users.length);

        DataSet user = users[0];
        assertEquals("[id, name, password]", Arrays.toString(user.getNames()));
        assertEquals("[13, Stiven Pupkin, pass2]", Arrays.toString(user.getValues()));

    }
}
