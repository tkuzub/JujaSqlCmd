package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class HelpTest {
    private View view;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        command = new Help(view);
    }

    @Test
    public void testCanProcessExitString() {
        //when
        boolean camProcess = command.canProcess("help");
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
    public void testHelp() {
        command.process("help");
        verify(view).write("Existing teams: ");
        verify(view).write("\tconnect|databaseName|userName|password");
    }

    @Test
    public void testValidationErrorWhenCountParametersIsNotEqualsOne() {
        try {
            command.process("help|qwe");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("command format 'help' but you entered: help|qwe",
                    e.getMessage());
        }
    }
}
