package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

public class UpdateTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Update(manager, view);
    }

    @Test
    public void testCanProcessWithParametersString() {
        boolean canProcess = command.canProcess("update|");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessWithOutParametersString() {
        boolean canProcess = command.canProcess("update");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweString() {
        boolean canProcess = command.canProcess("qwe");
        assertFalse(canProcess);
    }
}
