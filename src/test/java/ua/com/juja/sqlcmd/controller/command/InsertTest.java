package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class InsertTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Insert(manager, view);
    }

    @Test
    public void testCanProcessInsertWithParametersString() {
        boolean canProcess = command.canProcess("insert|");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessInsertWithoutParametersString() {
        boolean canProcess = command.canProcess("insert");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        boolean canProcess = command.canProcess("insert");
        assertFalse(canProcess);
    }

    @Test
    public void testInsert() {
        DataSet input = new DataSet();
        input.put("id", 100);
        input.put("name", "do_nothing");
        input.put("password", "+++++");
        command.process("insert|user_info|id|100|name|do_nothing|password|+++++");

        shouldPrint("[new data has been added to the 'user_info' ]");
    }

    @Test
    public void testValidationErrorWhenCountParametersNotEquals8() {
        try {
            command.process("insert|user_info|id|100|name|do_nothing|password");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("you entered the wrong number of parameters in the format" +
                    "expected 'insert|tableName|column1|value1|column2|value2|...|columnN|valueN'" +
                    " but you entered insert|user_info|id|100|name|do_nothing|password", e.getMessage());
        }
    }

    public void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
