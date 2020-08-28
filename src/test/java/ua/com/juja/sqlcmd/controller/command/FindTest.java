package ua.com.juja.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.DatabaseManager;
import ua.com.juja.sqlcmd.view.View;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FindTest {
    private View view;
    private DatabaseManager manager;

    @Before
    public void setup() {
        view = mock(View.class);
        manager = mock(DatabaseManager.class);
    }

    @Test
    public void testPrintTableData() {
        //given
        Command command = new Find(manager, view);
        when(manager.getTableColumns("user_info")).thenReturn(new String[]{"id", "name", "password"});
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
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        assertEquals("[===================," +
                " |id|name|password|," +
                " ===================," +
                " |10|jon|++++++|," +
                " |11|bob|-----|]", captor.getAllValues().toString());
    }
}
