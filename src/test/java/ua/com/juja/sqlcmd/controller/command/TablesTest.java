package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class TablesTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Tables(manager, view);
    }

    @Test
    public void testCanProcessTablesWithParametersString() {
        boolean canProcess = command.canProcess("tables");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        boolean canProcess = command.canProcess("qwe");
        assertFalse(canProcess);
    }

    @Test
    public void testTables() {
        when(manager.getTableNames()).thenReturn(new String[]{"user_info", "test"});
        command.process("tables");
        shouldPrint("[[user_info, test]]");
    }

    @Test
    public void testValidationErrorWhenCountParametersNotEqualsOne() {
        try {
            command.process("tables|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("command format 'tables' but you entered: tables|qwe", e.getMessage());
        }
    }

    public void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
