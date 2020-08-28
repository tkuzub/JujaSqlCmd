package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.controller.exception.ExitException;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;


public class ExitTest {
    private View view;

    @Before
    public void setup() {
        view = Mockito.mock(View.class);
    }

    @Test
    public void testCanProcessExitString() {
        //given
        Command command = new Exit(view);
        //when
        boolean camProcess = command.canProcess("exit");
        //then
        assertTrue(camProcess);
    }

    @Test
    public void testCanProcessQweString() {
        //given
        Command command = new Exit(view);
        //when
        boolean camProcess = command.canProcess("qwe");
        //then
        assertFalse(camProcess);
    }

    @Test
    public void testCanProcessExit_throwsExitException() {
        //given
        Command command = new Exit(view);
        //when
        try {
            command.process("exit");
            fail("Expected ExitException ");
        } catch (ExitException e) {
            //do nothing
        }
        //then
        Mockito.verify(view).write("Good bay!!!");
    }
}
