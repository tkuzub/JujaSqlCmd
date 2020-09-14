package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

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

    @Test
    public void testDelete() {
        //given
        DataSet user1 = new DataSet();
        user1.put("id", 101);
        user1.put("name", "jon");
        user1.put("password", "-----");
        //when
        List<DataSet> data = new LinkedList<>();
        data.add(user1);
        when(manager.getTableColumns("test")).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        when(manager.getTableData("test")).thenReturn(data);
        command.process("delete|test|id|100");
        //then
        shouldPrint("[===================," +
                " |id|name|password|," +
                " ===================," +
                " |101|jon|-----|]");
    }

    @Test
    public void testDeleteWhenEqualsColumnsNotInteger() {
        //given
        DataSet user1 = new DataSet();
        user1.put("id", 101);
        user1.put("name", "jon");
        user1.put("password", "-----");
        //when
        List<DataSet> data = new LinkedList<>();
        data.add(user1);
        when(manager.getTableColumns("test")).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        when(manager.getTableData("test")).thenReturn(data);
        command.process("delete|test|name|jon");
        //then
        shouldPrint("[===================," +
                " |id|name|password|," +
                " ===================," +
                " |101|jon|-----|]");
    }

    public void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
