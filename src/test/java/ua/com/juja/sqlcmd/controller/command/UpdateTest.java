package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DataSetImpl;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

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

    @Test
    public void testUpdate() {
        //given
        DataSet inputUpdate = new DataSetImpl();
        inputUpdate.put("id", 100);
        inputUpdate.put("name", "jon");
        inputUpdate.put("password", "+++++");
        List<DataSet> data = new LinkedList<>();
        data.add(inputUpdate);

        //when
        when(manager.getTableColumns("test")).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        when(manager.getTableData("test")).thenReturn(data);
        command.process("update|test|id|100|name|jon");

        //then
        shouldPrint("[===================," +
                    " |id|name|password|," +
                    " ===================," +
                    " |100|jon|+++++|]");
    }

    @Test
    public void testUpdateWhenEqualsColumnsNotInteger() {
        //given
        DataSet inputUpdate = new DataSetImpl();
        inputUpdate.put("id", 100);
        inputUpdate.put("name", "jon");
        inputUpdate.put("password", "-----");
        List<DataSet> data = new LinkedList<>();
        data.add(inputUpdate);

        //when
        when(manager.getTableColumns("test")).thenReturn(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        when(manager.getTableData("test")).thenReturn(data);
        command.process("update|test|name|jon|password|-----");

        //then
        shouldPrint("[===================," +
                " |id|name|password|," +
                " ===================," +
                " |100|jon|-----|]");
    }

    @Test
    public void testValidationErrorWhenCountParametersNotEquals8() {
        try {
            command.process("update|test|id|100|name");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("you entered the wrong number of parameters in the format" +
                    " expected 'update|tableName|column1|value1|column2|value2|...|columnN|valueN'" +
                    " but you entered update|test|id|100|name", e.getMessage());
        }
    }

    public void shouldPrint(String expected) {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals(expected, captor.getAllValues().toString());
    }
}
