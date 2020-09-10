package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FindTest {
    private View view;
    private DatabaseManager manager;
    private Command command;

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
        command = new Find(manager, view);
    }

    @Test
    public void testPrintTableData() {
        //given
        when(manager.getTableColumns("user_info")).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        DataSet user1 = new DataSet();
        user1.put("id", 10);
        user1.put("name", "jon");
        user1.put("password", "++++++");

        DataSet user2 = new DataSet();
        user2.put("id", 11);
        user2.put("name", "bob");
        user2.put("password", "-----");
        DataSet[] data = new DataSet[]{user1, user2};
        when(manager.getTableData("user_info")).thenReturn(data);
        //when
        command.process("find|user_info");
        //then
        shouldPrint("[===================," +
                          " |id|name|password|," +
                          " ===================," +
                          " |10|jon|++++++|," +
                          " |11|bob|-----|]");
    }

    public void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }

    @Test
    public void testCanProcessFindWithParametersString() {
        //when
        boolean camProcess = command.canProcess("find|");
        //then
        assertTrue(camProcess);
    }

    @Test
    public void testCanProcessFindWithoutParametersString() {
        //when
        boolean camProcess = command.canProcess("find");
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
    public void testPrintEmptyTableData() {
        when(manager.getTableColumns("user_info")).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        DataSet[] data = new DataSet[0];
        when(manager.getTableData("user_info")).thenReturn(data);
        //when
        command.process("find|user_info");
        //then
        shouldPrint("[===================," +
                           " |id|name|password|," +
                           " ===================]");
    }
}
