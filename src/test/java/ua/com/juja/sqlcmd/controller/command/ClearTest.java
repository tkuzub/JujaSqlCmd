package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ClearTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        command = new Clear(manager, view);
    }

    @Test
    public void testClearTable() {
        //when
        command.process("clear|user_info");
        //then
        verify(manager).clear("user_info");
        verify(view).write("the table is completely cleared of data");
    }

    @Test
    public void testCanProcessClearWithParametersString() {
        //when
        boolean camProcess = command.canProcess("clear|");
        //then
        assertTrue(camProcess);
    }

    @Test
    public void testCanProcessClearWithoutParametersString() {
        //when
        boolean camProcess = command.canProcess("clear");
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
    public void testValidationErrorWhenCountParametersIsLessThen2() {
        try {
            command.process("clear");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("command format 'clear|tableName' but you entered: clear", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThen2() {
        try {
            command.process("clear|user_info|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("command format 'clear|tableName' but you entered: clear|user_info|qwe", e.getMessage());
        }
    }
}
