package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(JDBCDatabaseManager.class);
        view = mock(View.class);
        command = new Create(manager, view);
    }

    @Test
    public void testCanProcessDropWithParametersString() {
        //when
        boolean camProcess = command.canProcess("create|");
        //then
        assertTrue(camProcess);
    }

    @Test
    public void testCanProcessDropWithoutParametersString() {
        //when
        boolean camProcess = command.canProcess("create");
        //then
        assertFalse(camProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //when
        boolean camProcess = command.canProcess("qwe|user_info");
        //then
        assertFalse(camProcess);
    }

    @Test
    public void testDropTable() {
        //given
        List<String> input = Arrays.asList("name", "password");
        String tableName = "some_table";
        //when
        command.process("create|some_table|name|password");
        //then
        verify(manager).create(tableName, input);
        verify(view).write("the table 'some_table' was created successfully");

        dropTableThatWasCreatedForTest();
    }

    public void dropTableThatWasCreatedForTest() {
        command = new Drop(manager, view);
        command.process("drop|some_table");
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThen2() {
        try {
            command.process("create|some_table|name");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("you entered the wrong number of parameters in the format expected " +
                    "'create|tableName|column1|column2|...|columnN' but you entered create|some_table|name",
                    e.getMessage());
        }
    }
}
