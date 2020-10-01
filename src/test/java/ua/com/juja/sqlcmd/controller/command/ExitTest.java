package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import ua.com.juja.sqlcmd.controller.exception.ExitException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;


public class ExitTest {
    private View view;
    private Command command;
    @Before
    public void setup() {
        view = mock(View.class);
        command = new Exit(view);
    }

    @Test
    public void testCanProcessExitString() {
        //when
        boolean camProcess = command.canProcess("exit");

        //then
        assertTrue(camProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //when
        boolean camProcess = command.canProcess("qwe");

        //then
        assertFalse(camProcess);
    }

    @Test
    public void testCanProcessExit_throwsExitException() {
        //when
        try {
            command.process("exit");
            fail("Expected ExitException ");
        } catch (ExitException e) {
            //do nothing
        }
        //then
        verify(view).write("Good bay!!!");
    }
}
