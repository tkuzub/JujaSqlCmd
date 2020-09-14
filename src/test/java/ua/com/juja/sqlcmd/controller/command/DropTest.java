package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DropTest {

    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(JDBCDatabaseManager.class);
        view = mock(View.class);
        command = new Drop(manager, view);
    }

    @Test
    public void testCanProcessDropWithParametersString() {
        //when
        boolean camProcess = command.canProcess("drop|");
        //then
        assertTrue(camProcess);
    }

    @Test
    public void testCanProcessDropWithoutParametersString() {
        //when
        boolean camProcess = command.canProcess("drop");
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
        command.process("drop|user_info");

        verify(manager).drop("user_info");
        verify(view).write("the table 'user_info' was deleted successfully");
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThen2() {
        try {
            command.process("drop");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("command format 'drop|tableName' but you entered: drop", e.getMessage());
        }
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThen2() {
        try {
            command.process("drop|user_info|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("command format 'drop|tableName' but you entered: drop|user_info|qwe", e.getMessage());
        }
    }
}
