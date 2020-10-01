package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectTest {
    private DatabaseManager manager;
    private View view;
    private Command command;

    @Before
    public void setup() {
        manager = mock(DatabaseManager.class);
        view = mock(View.class);
        command = new Connect(manager, view);
    }

    @Test
    public void testCanProcessConnectWithParametersString() {
        boolean canProcess = command.canProcess("connect|");
        assertTrue(canProcess);
    }

    @Test
    public void testCanProcessConnectWithOutParametersString() {
        boolean canProcess = command.canProcess("connect");
        assertFalse(canProcess);
    }

    @Test
    public void testCanProcessQweParametersString() {
        boolean canProcess = command.canProcess("qwe");
        assertFalse(canProcess);
    }

    @Test
    public void testValidationErrorWhenCountParametersIsLessThen4() {
        command.process("connect|sqlcmd_db|postgres");
        verify(view).write("FAIL for a cause incorrect number of entered parameters separated by '|' expected 4, but entered 3");
    }

    @Test
    public void testValidationErrorWhenCountParametersIsMoreThen4() {
        command.process("connect|sqlcmd_db|postgres|777|qwe");
        verify(view).write("FAIL for a cause incorrect number of entered parameters separated by '|' expected 4, but entered 5");
    }

    @Test
    public void testConnected() {
        command.process("connect|sqlcmd_db|postgres|777");
        verify(view).write("Success!!!");
    }
}
