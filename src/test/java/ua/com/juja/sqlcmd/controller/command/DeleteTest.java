package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DeleteTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Delete(manager, view);
    }

    @Test
    public void testCanProcessWithParametersString() {
        boolean canProcess = command.canProcess("delete|");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithOutParametersString() {
        boolean canProcess = command.canProcess("delete");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        boolean canProcess = command.canProcess("qwe");
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersNotEqualsFour() {
        try {
            command.process("delete|user_info|name|indigo|password");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("you entered the wrong number of parameters in the format" +
                    "expected 'delete|tableName|column|value'" +
                    " but you entered delete|user_info|name|indigo|password", e.getMessage());
        }
    }
}
