package ua.com.juja.sqlcmd.integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.launcher.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {
    private ConfigurableInputStream in;
    private ByteArrayOutputStream out;

    @Before
    public void setup() {
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExit() {
        //given
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testHelp() {
        //given
        in.add("help");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //help
                "Existing teams: \r\n" +
                "\tconnect|databaseName|userName|password\r\n" +
                "\t\t-for connect to database\r\n" +
                "\thelp\r\n" +
                "\t\t-for to display all existing commands on the screen\r\n" +
                "\ttables\r\n" +
                "\t\t-show a list of all tables of the database to which they are connected\r\n" +
                "\tcreate|tableName|column1|column2|...|columnN\r\n" +
                "\t\t-the command creates a new table with the given fields\r\n" +
                "\tfind|tableName\r\n" +
                "\t\t-to get the contents of the table 'tableName'\r\n" +
                "\tinsert|tableName|column1|value1|column2|value2|...|columnN|valueN\r\n" +
                "\t\t-to insert one row into a given 'tableName'\r\n" +
                "\tupdate|tableName|column1|value1|column2|value2\r\n" +
                "\t\t-to update the entry in the specified table by setting the value\r\n" +
                "\tdelete|tableName|column|value\r\n" +
                "\t\t-to deletes one or more records in the table for which the column = value condition is met\r\n" +
                "\tclear|tableName\r\n" +
                "\t\t-to clear all data from the 'tableName'\r\n" +
                "\tdrop|tableName\r\n" +
                "\t\t-to drop a 'tableName'\r\n" +
                //exit
                "\texit\r\n" +
                "\t\t-for exit with database\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testTablesWithoutConnect() {
        //given
        in.add("tables");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //tables
                "You cannot use the command 'tables', until you connect to the database in the format connect|databaseName|userName|password\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testFindWithoutConnect() {
        //given
        in.add("find|user_info");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //find|user_info
                "You cannot use the command 'find|user_info', until you connect to the database in the format connect|databaseName|userName|password\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testUnsupportedWithoutConnect() {
        //given
        in.add("unsupported");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //unsupported
                "You cannot use the command 'unsupported', until you connect to the database in the format connect|databaseName|userName|password\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testUnsupportedAfterConnect() {
        //given
        in.add("connect|sqlcmd_db|postgres|777");
        in.add("unsupported");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //connect
                "Success!!!\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //unsupported
                "non-existent command 'unsupported'\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testTablesAfterConnect() {
        //given
        in.add("connect|sqlcmd_db|postgres|777");
        in.add("tables");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //connect
                "Success!!!\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //tables
                "[user_info, test]\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testFindAfterConnect() {
        //given
        in.add("connect|sqlcmd_db|postgres|777");
        in.add("find|user_info");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //connect
                "Success!!!\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //find|user_info
                "===================\r\n" +
                "|id|name|password|\r\n" +
                "===================\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    @Test
    public void testConnectAfterConnect() {
        //given
        in.add("connect|sqlcmd_db|postgres|777");
        in.add("tables");
        in.add("connect|test|postgres|777");
        in.add("tables");
        in.add("exit");
        //when
        Main.main(new String[0]);
        //then
        assertEquals("Hello user!!!\r\n" +
                "Please enter the database name, username and password in the format connect|databaseName|userName|password\r\n" +
                //connect|sqlcmd_db
                "Success!!!\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //tables
                "[user_info, test]\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //connect|test
                "Success!!!\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //tables
                "[qwerty]\r\n" +
                "Enter an existing command (or command 'help' for help)\r\n" +
                //exit
                "Good bay!!!\r\n", getData());
    }

    private String getData() {
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}
